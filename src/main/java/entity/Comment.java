package entity;


import boundary.request.UpdateComment;
import boundary.request.UpdatePost;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoEntity;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.bson.types.ObjectId;

import javax.json.bind.annotation.JsonbTransient;
import javax.ws.rs.NotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public class Comment extends ReactivePanacheMongoEntity {

    public String title;
    public String content;
    public LocalDateTime creationDate;
    @JsonbTransient
    public String postId;

    public static Uni<Comment> updateComment(String id, UpdateComment updateComment) {
        Uni<Comment> commentUni = findById(new ObjectId(id));

        return commentUni.call(comment -> {

            comment.content = updateComment.getContent();

            Uni<Post> uni = Post.findById(new ObjectId(comment.postId));
            return uni.call(posts -> {
                if (posts != null) {
                    Optional<Comment> com = posts.comments.stream()
                            .filter(comment1 -> comment1.equals(comment)).findFirst();
                    if (com.isPresent()) {
                        com.get().content = updateComment.getContent();
                    }
                }
                return Uni.createFrom().item(comment);
            }).chain(post -> post.persistOrUpdate());
        }).chain(comment -> {
            if (comment == null) {
                throw new NotFoundException();
            }
            return comment.persistOrUpdate();
        });


    }

    public static Uni<Void> deleteComment(String commentId) {
        Uni<Comment> commentUni = findById(new ObjectId(commentId));

        return commentUni.call(comment -> {

            Uni<Post> uni = Post.findById(new ObjectId(comment.postId));
            return uni.call(posts -> {
                if (posts != null) {
                    posts.comments.remove(comment);
                }
                return Uni.createFrom().item(comment);
            }).chain(post -> post.persistOrUpdate());
        }).chain(comment -> {
            if (comment == null) {
                throw new NotFoundException();
            }
            return comment.delete();
        });
    }

    public static Multi<Comment> streamAllComments() {
        return streamAll();
    }

    public static Multi<Comment> streamAllCommentsByPostId(String postId) {
        return stream("postId", postId);
    }


    @Override
    public boolean equals(Object c) {
        if (c == null) return false;
        Comment comp = ((Comment) c);
        return comp.id.equals(id);
    }
}

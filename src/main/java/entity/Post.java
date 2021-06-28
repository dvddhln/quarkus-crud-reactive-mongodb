package entity;

import boundary.request.UpdatePost;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoEntity;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.bson.types.ObjectId;

import javax.ws.rs.NotFoundException;
import java.time.LocalDateTime;
import java.util.List;


public class Post extends ReactivePanacheMongoEntity {

    public String title;
    public String content;
    public String author;
    public LocalDateTime creationDate;
    public List<Comment> comments;


    public static Uni<Post> updatePost(String id, UpdatePost updatePost) {
        Uni<Post> postUni = Post.findById(new ObjectId(id));
        return postUni
                .onItem().transform(post -> {
                    post.content = updatePost.getContent();
                    post.title = updatePost.getTitle();
                    return post;
                }).call(post -> post.persistOrUpdate());
    }


    public static Uni<Post> addCommentToPost(Comment comment, String postId) {
        Uni<Post> postUni = findById(new ObjectId(postId));

        return postUni.onItem().transform(post -> {

            if (post.comments == null) {
                post.comments = List.of(comment);
            } else {
                post.comments.add(comment);
            }
            comment.creationDate = LocalDateTime.now();
            comment.postId = postId;
            return post;
        }).call(post -> comment.persist().chain(() -> post.persistOrUpdate()));
    }

    public static Uni<Void> deletePost(String postId) {
        Uni<Post> postUni = findById(new ObjectId(postId));
        Multi<Comment> commentsUni = Comment.streamAllCommentsByPostId(postId);

        return postUni.call(post -> commentsUni.onItem().call(comment -> comment.delete())
                .collect().asList()).chain(post -> {
            if (post == null) {
                throw new NotFoundException();
            }
            return post.delete();
        });
    }

    public static Multi<Post> streamAllPosts() {
        return streamAll();
    }

}

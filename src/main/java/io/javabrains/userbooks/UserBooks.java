package io.javabrains.userbooks;

import org.springframework.data.cassandra.core.mapping.*;

import java.time.LocalDate;

@Table(value = "book_by_user_and_bookid")
public class UserBooks {

    @PrimaryKey
    private UserBooksPrimaryKey primaryKey;

    @Column(value = "started_date")
    @CassandraType(type = CassandraType.Name.DATE)
    private LocalDate startedDate;

    @Column(value = "finished_date")
    @CassandraType(type = CassandraType.Name.DATE)
    private LocalDate finishedDate;

    @Column(value = "reading_status")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String readingStatus;

    @Column(value = "rating")
    @CassandraType(type = CassandraType.Name.INT)
    private int rating;

    public UserBooksPrimaryKey getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(UserBooksPrimaryKey primaryKey) {
        this.primaryKey = primaryKey;
    }

    public LocalDate getStartedDate() {
        return startedDate;
    }

    public void setStartedDate(LocalDate startedDate) {
        this.startedDate = startedDate;
    }

    public LocalDate getFinishedDate() {
        return finishedDate;
    }

    public void setFinishedDate(LocalDate finishedDate) {
        this.finishedDate = finishedDate;
    }

    public String getReadingStatus() {
        return readingStatus;
    }

    public void setReadingStatus(String readingStatus) {
        this.readingStatus = readingStatus;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "UserBooks{" +
                "primaryKey=" + primaryKey +
                ", startedDate=" + startedDate +
                ", finishedDate=" + finishedDate +
                ", readingStatus='" + readingStatus + '\'' +
                ", rating=" + rating +
                '}';
    }
}

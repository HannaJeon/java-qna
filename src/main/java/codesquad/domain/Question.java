package codesquad.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.validation.constraints.Size;

import codesquad.CannotDeleteException;
import codesquad.UnAuthorizedException;
import org.hibernate.annotations.Where;

import codesquad.dto.QuestionDto;
import support.domain.AbstractEntity;
import support.domain.UrlGeneratable;

@Entity
public class Question extends AbstractEntity implements UrlGeneratable {
    @Size(min = 3, max = 100)
    @Column(length = 100, nullable = false)
    private String title;

    @Size(min = 3)
    @Lob
    private String contents;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    @Where(clause = "deleted = false")
    @OrderBy("id ASC")
    private List<Answer> answers = new ArrayList<>();

    private boolean deleted = false;

    public Question() {
    }

    public Question(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }

    public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
    }

    public User getWriter() {
        return writer;
    }

    public void writeBy(User loginUser) {
        this.writer = loginUser;
    }

    public void addAnswer(Answer answer) {
        answer.toQuestion(this);
        answers.add(answer);
    }

    public boolean isOwner(User loginUser) {
        return writer.equals(loginUser);
    }

    public boolean isDeleted() {
        return deleted;
    }

    public List<DeleteHistory> delete(User loginUser) throws CannotDeleteException {
        if (!isOwner(loginUser))
            throw new UnAuthorizedException();

        if (!canDelete(loginUser))
            throw new CannotDeleteException("질문을 삭제할 수 없습니다.");

        this.deleted = true;

        List<DeleteHistory> deleteHistories = deleteAnswers();
        deleteHistories.add(new DeleteHistory(ContentType.QUESTION, getId(), loginUser, LocalDateTime.now()));

        return deleteHistories;
    }

    private List<DeleteHistory> deleteAnswers() throws CannotDeleteException {
        List<DeleteHistory> deleteHistories = new ArrayList<>();

        for (Answer answer : answers) {
            deleteHistories.add(answer.delete(writer));
        }
        return deleteHistories;
    }

    private boolean canDelete(User writer) {
        for (Answer answer : answers) {
            if (!answer.isOwner(writer))
                return false;
        }
        return true;
    }

    @Override
    public String generateUrl() {
        return String.format("questions/%d", getId());
    }

    public QuestionDto toQuestionDto() {
        if (isDeleted()) {
            return null;
        }
        return new QuestionDto(getId(), this.title, this.contents);
    }

    public void update(User loginUser, Question target) {
        if (!isOwner(loginUser))
            throw new UnAuthorizedException();

        this.title = target.title;
        this.contents = target.contents;
    }

    @Override
    public String toString() {
        return "Question [id=" + getId() + ", deleted=" + isDeleted() + ", title=" + title + ", contents=" + contents + ", writer=" + writer + "]";
    }
}

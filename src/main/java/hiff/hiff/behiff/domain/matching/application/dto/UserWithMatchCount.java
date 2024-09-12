package hiff.hiff.behiff.domain.matching.application.dto;

import hiff.hiff.behiff.domain.user.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserWithMatchCount implements Comparable<UserWithMatchCount> {

    private User user;
    private int count;

    public void increaseCount() {
        count++;
    }

    @Override
    public int compareTo(UserWithMatchCount other) {
        return Integer.compare(this.count, other.count);
    }
}

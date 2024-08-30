package hiff.hiff.behiff.domain.matching.application.service;

import hiff.hiff.behiff.domain.user.domain.entity.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

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

package lepdv.todolistweb.repository;

import lepdv.todolistweb.dto.filter.UserFilter;
import lepdv.todolistweb.entity.User;

import java.util.List;

public interface FilterUserRepository {

    public List<User> findAllByFilter(UserFilter filter);
}

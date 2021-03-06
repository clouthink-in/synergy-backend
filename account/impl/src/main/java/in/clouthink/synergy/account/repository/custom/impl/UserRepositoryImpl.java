package in.clouthink.synergy.account.repository.custom.impl;

import in.clouthink.synergy.account.domain.model.Group;
import in.clouthink.synergy.account.domain.model.Role;
import in.clouthink.synergy.account.domain.model.User;
import in.clouthink.synergy.account.domain.request.UserSearchRequest;
import in.clouthink.synergy.account.domain.request.UsernameSearchRequest;
import in.clouthink.synergy.account.repository.custom.UserRepositoryCustom;
import in.clouthink.synergy.shared.repository.custom.impl.AbstractCustomRepositoryImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

@Repository
public class UserRepositoryImpl extends AbstractCustomRepositoryImpl implements UserRepositoryCustom {

    @Override
    public Page<User> queryPage(UsernameSearchRequest queryRequest) {
        int start = queryRequest.getStart();
        int limit = queryRequest.getLimit();
        Query query = new Query();
        if (!StringUtils.isEmpty(queryRequest.getUsername())) {
            query.addCriteria(Criteria.where("username").regex(queryRequest.getUsername()));
        }
        query.addCriteria(Criteria.where("archived").ne(true));

        PageRequest pageable = new PageRequest(start, limit, new Sort(Sort.Direction.ASC, "rank", "username"));
        query.with(pageable);
        long count = mongoTemplate.count(query, User.class);
        List<User> list = mongoTemplate.find(query, User.class);

        return new PageImpl<>(list, pageable, count);
    }

    @Override
    public Page<User> queryPage(Group group, UsernameSearchRequest queryRequest) {
        int start = queryRequest.getStart();
        int limit = queryRequest.getLimit();
        Query query = new Query();
//        query.addCriteria(Criteria.where("groups").in(group));
        if (!StringUtils.isEmpty(queryRequest.getUsername())) {
            query.addCriteria(Criteria.where("username").regex(queryRequest.getUsername()));
        }
        query.addCriteria(Criteria.where("archived").ne(true));

        PageRequest pageable = new PageRequest(start, limit, new Sort(Sort.Direction.ASC, "rank", "username"));
        query.with(pageable);
        long count = mongoTemplate.count(query, User.class);
        List<User> list = mongoTemplate.find(query, User.class);

        return new PageImpl<>(list, pageable, count);
    }

    @Override
    public Page<User> queryPage(Role role, UserSearchRequest queryRequest) {
        int start = queryRequest.getStart();
        int limit = queryRequest.getLimit();
        Query query = new Query();
//        query.addCriteria(Criteria.where("roles").in(role));
        if (!StringUtils.isEmpty(queryRequest.getUsername())) {
            query.addCriteria(Criteria.where("username").regex(queryRequest.getUsername()));
        }
        query.addCriteria(Criteria.where("archived").ne(true));

        PageRequest pageable = new PageRequest(start, limit, new Sort(Sort.Direction.DESC, "username"));
        query.with(pageable);
        long count = mongoTemplate.count(query, User.class);
        List<User> list = mongoTemplate.find(query, User.class);

        return new PageImpl<>(list, pageable, count);
    }

    @Override
    public Page<User> queryPage(UserSearchRequest queryRequest) {
        int start = queryRequest.getStart();
        int limit = queryRequest.getLimit();
        Query query = buildQuery(queryRequest);
        query.addCriteria(Criteria.where("archived").ne(true));

        PageRequest pageable = new PageRequest(start, limit, new Sort(Sort.Direction.ASC, "rank", "username"));
        query.with(pageable);
        long count = mongoTemplate.count(query, User.class);
        List<User> list = mongoTemplate.find(query, User.class);

        return new PageImpl<>(list, pageable, count);
    }

    @Override
    public Page<User> queryArchivedUsers(UserSearchRequest queryRequest) {
        int start = queryRequest.getStart();
        int limit = queryRequest.getLimit();
        Query query = buildQuery(queryRequest);
        query.addCriteria(Criteria.where("archived").is(true));

        PageRequest pageable = new PageRequest(start, limit, new Sort(Sort.Direction.DESC, "archivedAt"));
        query.with(pageable);
        long count = mongoTemplate.count(query, User.class);
        List<User> list = mongoTemplate.find(query, User.class);

        return new PageImpl<>(list, pageable, count);
    }

    private Query buildQuery(UserSearchRequest request) {
        Query query = new Query();
        if (!StringUtils.isEmpty(request.getUsername())) {
            query.addCriteria(Criteria.where("username").regex(request.getUsername()));
        }
        if (!StringUtils.isEmpty(request.getTelephone())) {
            query.addCriteria(Criteria.where("telephone").regex(request.getTelephone()));
        }
        if (!StringUtils.isEmpty(request.getEmail())) {
            query.addCriteria(Criteria.where("email").regex(request.getEmail()));
        }
        if (request.getEnabled() != null) {
            query.addCriteria(Criteria.where("enabled").is(request.getEnabled().booleanValue()));
        }

        return query;
    }

}

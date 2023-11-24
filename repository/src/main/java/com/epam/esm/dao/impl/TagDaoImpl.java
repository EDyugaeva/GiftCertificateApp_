package com.epam.esm.dao.impl;

import com.epam.esm.dao.Column;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.mapper.TagMapper;
import com.epam.esm.exceptions.DataNotFoundException;
import com.epam.esm.model.Tag;
import org.slf4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.util.List;

import static com.epam.esm.exceptions.ExceptionCodes.NOT_FOUND_TAG;

@Repository
public class TagDaoImpl implements TagDao {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(TagDao.class);
    private final JdbcTemplate jdbcTemplateObject;
    private static final String INSERT = "insert into Tag (name) values (?)";
    private static final String SELECT_BY_ID = "select * from Tag where id = ?";
    private static final String SELECT_ALL = "select * from Tag";
    private static final String DELETE = "delete from Tag where id = ?";

    public TagDaoImpl(JdbcTemplate jdbcTemplateObject) {
        this.jdbcTemplateObject = jdbcTemplateObject;
    }

    @Override
    @Transactional
    public Tag saveTag(Tag tag) {
        log.info("Saving tag " + tag);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplateObject.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(INSERT, new String[]{Column.ID});
                    ps.setString(1, tag.getName());
                    return ps;
                },
                keyHolder);
        tag.setId(keyHolder.getKeyAs(Long.class));
        return tag;
    }

    @Override
    public Tag getTag(long id) {
        try {
            log.info("Trying to get tag with id = {}", id);
            return jdbcTemplateObject.queryForObject(SELECT_BY_ID, new Object[]{id}, new TagMapper());
        } catch (EmptyResultDataAccessException e) {
            log.error("Error while getting tag with id = {}", id, e);
            throw new DataNotFoundException(NOT_FOUND_TAG.getErrorCode());
        }
    }

    @Override
    public List<Tag> getTags() {
        try {
            log.info("Trying to get all tags");
            return jdbcTemplateObject.query(SELECT_ALL, new TagMapper());
        } catch (EmptyResultDataAccessException e) {
            log.error("Error while getting all tags", e);
            throw new DataNotFoundException(NOT_FOUND_TAG.getErrorCode());
        }
    }

    @Override
    @Transactional
    public void deleteTag(long id) {
        log.info("Trying to delete tag with id = {}", id);
        jdbcTemplateObject.update(DELETE, id);
    }
}
package net.bnijik.schooldbcli.dao.group;

import net.bnijik.schooldbcli.dao.Page;
import net.bnijik.schooldbcli.model.Group;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@ContextConfiguration(classes = {GroupQueries.class, JdbcGroupDaoTestConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "/sql/drop_create_tables.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = {"/sql/clear_tables.sql", "/sql/sample_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class JdbcGroupDaoTest {

    @Autowired
    private JdbcGroupDao groupDao;

    @Test
    @DisplayName("when saving group of certain name should save group")
    void whenSavingGroupOfCertainNameShouldSaveGroup() {

        final Group expected = new Group(4, "AA-11");

        assertEquals(expected.groupId(), groupDao.save(expected));
    }

    @Test
    @DisplayName("when getting a group by ID should return the correct group")
    void whenGettingGroupByIdShouldReturnCorrectGroup() {
        Group expectedGroup = new Group(1L, "BB-22");

        Optional<Group> retrievedGroupWrapper = groupDao.findById(expectedGroup.groupId());

        assertTrue(retrievedGroupWrapper.isPresent(), "Group is not present");
        assertEquals(expectedGroup, retrievedGroupWrapper.get(), "Retrieved group details are incorrect");
    }


    @Test
    @DisplayName("when getting a group by name should return the correct group")
    void whenGettingGroupByNameShouldReturnCorrectGroup() {
        Group createdGroup = new Group(1, "BB-22");

        Optional<Group> retrievedGroupWrapper = groupDao.findByName("BB-22");

        assertTrue(retrievedGroupWrapper.isPresent(), "Group is not present");
        assertEquals(createdGroup, retrievedGroupWrapper.get(), "Retrieved group details are incorrect");
    }

    @Test
    @DisplayName("when getting all groups should return a stream of all groups")
    void whenGettingAllGroupsShouldReturnStreamOfAllGroups() {
        Group expectedGroup1 = new Group(1L, "BB-22");
        Group expectedGroup2 = new Group(2L, "CC-33");
        Group expectedGroup3 = new Group(3L, "Group to Delete");

        final Stream<Group> groupStream = groupDao.findAll(Page.of(1, 5));

        final long count = groupStream.filter(c -> c.equals(expectedGroup1) ||
                c.equals(expectedGroup2) ||
                c.equals(expectedGroup3)).count();
        assertEquals(3, count);
    }

    @Test
    @DisplayName("when getting groups by max student count should return correct groups")
    void whenGettingGroupsByMaxStudentCountShouldReturnCorrectGroups() {
        Group group1 = new Group(1, "BB-22");
        Group group2 = new Group(2, "CC-33");
        Group group3 = new Group(3, "Group to Delete");

        List<Group> groups = groupDao.findAllByMaxStudentCount(1, Page.of(1, 5)).toList();

        assertEquals(2, groups.size());
        assertTrue(groups.contains(group1));
        assertTrue(groups.contains(group3));
        assertFalse(groups.contains(group2));
    }

    @Test
    @DisplayName("when deleting a group should remove it from the database")
    void whenDeletingGroupShouldRemoveFromDatabase() {
        Group groupToDelete = new Group(3, "Group to Delete");

        assertTrue(groupDao.delete(groupToDelete.groupId()));
    }

    @Test
    @DisplayName("when updating existing group should update group")
    void whenUpdatingExistingGroupShouldUpdateGroup() {
        assertTrue(groupDao.update(new Group(3L, "Modified group name"), 3L));
    }

}
package net.bnijik.schooldbcli.model;

public record Group(long groupId, String groupName) {
    @Override
    public String toString() {
        return "Group{" + "groupId=" + groupId + ", groupName='" + groupName + '\'' + '}';
    }
}

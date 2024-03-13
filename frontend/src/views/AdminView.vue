<template>
    <h1>Admin page</h1>
    <InvitationForm />
    <ProjectComponent />
    <AdminUserList />
    <UserTimeRegistrations />
</template>

<script>
import InvitationForm from '../components/InvitationForm.vue';
import AdminUserList from '../components/AdminUserList.vue';
import ProjectComponent from '../components/ProjectComponent.vue';
import UserTimeRegistrations from '../components/UserTimeRegistrations.vue';

export default {
    components: {
        InvitationForm,
        AdminUserList,
        ProjectComponent,
        UserTimeRegistrations,
    },
};
</script>
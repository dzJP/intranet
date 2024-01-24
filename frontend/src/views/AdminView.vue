<template>
    <h1>Admin page</h1>
    <InvitationForm />
    <ProjectComponent />
    <AdminUserList />
</template>

<script>
import InvitationForm from '../components/InvitationForm.vue';
import AdminUserList from '../components/AdminUserList.vue';
import ProjectComponent from '../components/ProjectComponent.vue';

export default {
    components: {
        InvitationForm,
        AdminUserList,
        ProjectComponent,
    },
};
</script>
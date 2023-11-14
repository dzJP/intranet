<template>
    <h1>Admin page</h1>
    <InvitationForm />
    <AdminUserList />
</template>

<script>
import InvitationForm from '../components/InvitationForm.vue';
import AdminUserList from '../components/AdminUserList.vue';

export default {
    components: {
        InvitationForm,
        AdminUserList,
    },
};
</script>
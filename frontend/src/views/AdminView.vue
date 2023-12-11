<template>
    <div>
        <h1>Admin page</h1>
        <InvitationForm />
        <AdminUserList />
        <NewsForm />
    </div>
</template>

<script>
import InvitationForm from '@/components/InvitationForm.vue';
import AdminUserList from '@/components/AdminUserList.vue';
import NewsForm from '@/components/NewsForm.vue';

export default {
    components: {
        InvitationForm,
        AdminUserList,
        NewsForm
    },
};
</script>
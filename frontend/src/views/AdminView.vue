<template>
    <div>
        <h1>Admin page</h1>
        <InvitationForm />
        <AdminUserList />
        <NewsEdit />
        <NewsCreate />
    </div>
</template>

<script>
import InvitationForm from '@/components/InvitationForm.vue';
import AdminUserList from '@/components/AdminUserList.vue';
import NewsCreate from '@/components/NewsCreate.vue';
import NewsEdit from '@/components/NewsEdit.vue';


export default {
    components: {
        InvitationForm,
        AdminUserList,
        NewsCreate,
        NewsEdit
    },
};
</script>
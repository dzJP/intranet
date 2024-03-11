<template>
    <div>
        <h1>Admin page</h1>
        <InvitationForm />
        <NewsCreate />
        <!-- <div class="newsedit-and-userlist-container"> -->
            <NewsList :search-query="searchQuery" />
            <AdminUserList />
        <!-- </div> -->
    </div>
</template>

<script>
import InvitationForm from '@/components/InvitationForm.vue';
import AdminUserList from '@/components/AdminUserList.vue';
import NewsCreate from '@/components/NewsCreate.vue';
import NewsList from '@/components/NewsList.vue';

export default {
    components: {
        InvitationForm,
        AdminUserList,
        NewsCreate,
        NewsList,
    },
};
</script>

<style scoped>
/* .newsedit-and-userlist-container {
    display: flex;
    min-width: auto;
} */
</style>

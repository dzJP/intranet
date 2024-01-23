<template>
    <div>
        <h1>Admin page</h1>
        <InvitationForm />
        <NewsCreate />
        <SearchBarNews @search="updateSearchQuery" />
        <div class="edit-and-userlist-container">
            <NewsList />
            <AdminUserList />
        </div>
    </div>
</template>

<script>
import InvitationForm from '@/components/InvitationForm.vue';
import AdminUserList from '@/components/AdminUserList.vue';
import NewsCreate from '@/components/NewsCreate.vue';
import NewsList from '@/components/NewsList.vue';
import SearchBarNews from '@/components/SearchBarNews.vue';

export default {
    components: {
    InvitationForm,
    AdminUserList,
    NewsCreate,
    NewsList,
    SearchBarNews
},
};
</script>

<style scoped>
.edit-and-userlist-container {
    display: flex;
    margin-top: 20px;
    max-width: fit-content;
}
</style>
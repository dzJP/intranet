<template>
    <div class="user-list-container">
        <SearchBar @search="updateSearchQuery" />
        <ul class="user-list">
            <li v-for="user in filteredUsers" :key="user.email" class="user-item">
                <router-link :to="`/user/${user.email}`" class="user-link">
                    {{ user.firstName }} {{ user.lastName }}
                </router-link>
            </li>
        </ul>
    </div>

    <div class="mx-auto">
        <button class="btn btn-primary" @click="loadMoreUsers" v-if="filteredUsers.length >= 10">
            Load More
        </button>
    </div>
</template>

<script>
import { ref, computed } from 'vue';
import { useUserStore } from '@/stores/user';
import SearchBar from '@/components/SearchBar.vue';

export default {
    setup() {
        const userStore = useUserStore();
        const searchQuery = ref('');
        const displayedUsersCount = ref(10);

        const filteredUsers = computed(() => {
            const allUsers = userStore.searchUsers(searchQuery.value);
            return allUsers.slice(0, displayedUsersCount.value);
        });

        const updateSearchQuery = (value) => {
            searchQuery.value = value;
        };

        const loadMoreUsers = () => {
            displayedUsersCount.value += 10;
        };
        return {
            searchQuery,
            filteredUsers,
            updateSearchQuery,
            loadMoreUsers,
            user: useUserStore,
        };
    },
    components: { SearchBar }
};
</script>

<style scoped>
.user-list-container {
    max-width: 600px;
    margin: auto;
}

.user-list {
    list-style: none;
    padding: 0;
}

.user-item {
    margin-bottom: 5px;
    background-color: #f9f9f9;
    padding: 5px;
    border-radius: 10px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.user-link {
    text-decoration: none;
    color: #333;
    font-weight: bold;
    display: block;
    transition: background-color 0.3s ease-in-out;
    padding: 10px;
    border-radius: 5px;
}

.user-link:hover {
    background-color: #f0f0f0;
}
</style>
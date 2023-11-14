<template>
    <div class="user-list-container">
        <h2>All Users</h2>
        <input v-model="searchQuery" placeholder="Search by name" class="search-input" />
        <ul class="user-list">
            <li v-for="user in filteredUsers" :key="user.email" class="user-item">
                <router-link :to="`/user/${user.username || user.email}`" class="user-link">
                    {{ user.firstName }} {{ user.lastName }}
                </router-link>
            </li>
        </ul>
    </div>
</template>

<style scoped>
.user-list-container {
    max-width: 600px;
    margin: auto;
}

h2 {
    color: #333;
    font-size: 1.5rem;
    margin-bottom: 10px;
}

.search-input {
    width: 100%;
    padding: 8px;
    margin-bottom: 20px;
    box-sizing: border-box;
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

<script>
import { ref, onMounted, computed, watch } from 'vue';
import axios from 'axios';

export default {
    props: {
        apiUrl: {
            type: String,
            required: true,
        },
    },
    setup(props) {
        const users = ref([]);
        const searchQuery = ref('');

        const fetchUsers = async () => {
            try {
                const response = await axios.get(props.apiUrl, {
                    headers: {
                        Authorization: `Bearer ${localStorage.getItem('token')}`,
                    },
                });

                users.value = response.data;
            } catch (error) {
                console.error('Error fetching users:', error);
            }
        };

        onMounted(() => {
            fetchUsers();
        });

        // Derive filtered users based on searchQuery
        const filteredUsers = computed(() => {
            if (!searchQuery.value) {
                return users.value;
            } else {
                // Case-insensitive search by first name or last name
                return users.value.filter(
                    (user) =>
                        user.firstName.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
                        user.lastName.toLowerCase().includes(searchQuery.value.toLowerCase())
                );
            }
        });

        // Watcher to trigger refetch when apiUrl changes
        watch(() => props.apiUrl, () => {
            fetchUsers();
        });

        return {
            searchQuery,
            filteredUsers,
        };
    },
};
</script>
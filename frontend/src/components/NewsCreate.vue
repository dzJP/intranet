<template>
    <button v-if="userRole === 'ROLE_ADMIN'" class="create-toggle btn btn-primary"
    @click="toggleFormVisibility">Create News</button>
    <div v-else>
        <p>You do not have permission to create news articles.</p>
    </div>
    <div v-if="isFormVisible" class="create-news">
        <i class="toggle-news bi bi-x" @click="toggleFormVisibility"></i>
        <form @submit.prevent="createNews">
            <label>Date</label>
            <input v-model="newNews.date" type="datetime-local" required>
            <label>Deadline</label>
            <input v-model="newNews.deadline" type="datetime-local">
            <label>Subject</label>
            <input v-model="newNews.subject" type="text" required>
            <label>Message</label>
            <textarea v-model="newNews.message" required></textarea>
            <button type="submit" class="btn btn-primary">Create News</button>
        </form>
    </div>
</template>

<script>
import { ref, getCurrentInstance, watch } from 'vue';
import { useNewsStore } from '@/stores/news';
import { useAuthStore } from '@/stores/auth';

export default {
    setup() {
        const newsStore = useNewsStore();
        const newNews = ref({ subject: '', message: '', date: '', deadline: '' });
        const isFormVisible = ref(false);

        const { emit } = getCurrentInstance();
        const authStore = useAuthStore();
        const userRole = ref(authStore.role);

        watch(() => authStore.role, (newRole) => {
            userRole.value = newRole;
        });

        const createNews = async () => {
            try {
                const createdNews = await newsStore.createNews(newNews.value);
                console.log('Created news:', createdNews.data);
                
                await newsStore.getAllNews();

                emit('news-created');
                newNews.value = { subject: '', message: '', date: '', deadline: '' };
            } catch (error) {
                console.error('Error creating news:', error);
            }
        };

        const toggleFormVisibility = () => {
            isFormVisible.value = !isFormVisible.value;
        };

        return {
            newNews,
            createNews,
            userRole,
            isFormVisible,
            toggleFormVisibility,
        };
    },
};
</script>

<style scoped>

.create-toggle {
  position: relative;
  margin-left: 310px;
}

.create-news {
    position: fixed;
    top: 35%;
    left: 50%;
    transform: translate(-50%, -50%);
    font-size: 16px;
    min-height: 260px;
    width: 450px;
    background: #111C44;
    color: #fff;
    padding: 20px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    border-radius: 9px;
    z-index: 1;
}

.create-news button.btn-primary {
    background-color: #3498db;
    color: #fff;
    border: 1px solid #3498db;
    padding: 5px 50px;
    cursor: pointer;
}

.create-news button.btn-primary:hover {
    background-color: #0bb4e7;
    border-color: #2980b9;
}

.create-news button {
    margin: 10px auto;
    width: 100%;
}

.create-news label {
    display: block;
    margin-top: 10px;
}

.create-news input {
    width: 100%;
    height: 40px;
    border-radius: 5px;
    outline: none;
}

.create-news textarea {
    min-height: 70px;
    width: 100%;
    border-radius: 5px;
    outline: none;
}

.toggle-news   {
    position: fixed;
    top: 0;
    right: 0;
    height: 35px;
    width: 40px;
    }

    i {
    font-size: 25px;
    color: #ffffff;
    }

    .toggle-news:hover {
    background-color: #df3232;
    color: #fff;
    cursor: pointer;
    transition: 0.3s;
    border-top-right-radius: 9px;
    }

</style>

<template>
    <div v-if="userRole === 'ROLE_ADMIN'" class="card-body news px-0 pt-0 pb-2">
        <form @submit.prevent="createNews">
            <label>Subject</label>
            <input v-model="newNews.subject" type="text" required>
            <label>Message</label>
            <textarea v-model="newNews.message" required></textarea>
            <label>Date</label>
            <input v-model="newNews.date" type="date" required>
            <label>Deadline</label>
            <input v-model="newNews.deadline" type="date" required>
            <button type="submit" class="btn btn-primary">Create News</button>
        </form>
    </div>
    <div v-else>
        <p>You do not have permission to create news articles.</p>
    </div>
</template>

<script>
import { ref, getCurrentInstance } from 'vue';
import { useNewsStore } from '@/stores/news';
import { useAuthStore } from '@/stores/auth';

export default {
    setup() {
        const newsStore = useNewsStore();
        const newNews = ref({ subject: '', message: '', date: '', deadline: '' });

        const { emit } = getCurrentInstance();
        const authStore = useAuthStore();
        const userRole = authStore.role;

        const createNews = async () => {
            try {
                const createdNews = await newsStore.createNews(newNews.value);
                console.log('Created news:', createdNews.data);
                emit('news-created');
                newNews.value = { subject: '', message: '', date: '', deadline: '' };
            } catch (error) {
                console.error('Error creating news:', error);
            }
        };

        return {
            newNews,
            createNews,
            userRole,
        };
    },
};
</script>
  
<style scoped>
.card-body.news {
    margin-top: 20px;
    padding: 10px;
    border: 1px solid #ccc;
    border-radius: 5px;
    background-color: #f0f0f0;
    width: 100%;
    position: relative;
}

.card-body.news button.btn-primary {
    background-color: #3498db;
    color: #fff;
    border: 1px solid #3498db;
    padding: 5px 50px;
    cursor: pointer;
}

.card-body.news button.btn-primary:hover {
    background-color: #0bb4e7;
    border-color: #2980b9;
}

.card-body.news button {
    margin-right: 10px;
}

.card-body.news label {
    display: block;
    margin-top: 8px;
}

.card-body.news input,
.card-body.news textarea {
    width: 15%;
}
</style>
  
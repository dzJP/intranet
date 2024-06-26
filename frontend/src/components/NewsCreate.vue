<template>
    <button v-if="userRole === 'ROLE_ADMIN'" class="create-news-button" @click="toggleFormVisibility">CREATE
        NEWS</button>
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
        const newNews = ref({ subject: '', message: '', date: formatDate(new Date()), deadline: formatDeadline(new Date()) });
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

        function formatDate(date) {
            const year = date.getFullYear();
            let month = date.getMonth() + 1;
            let day = date.getDate();
            let hours = date.getHours();
            let minutes = date.getMinutes();

            if (month < 10) {
                month = '0' + month;
            }
            if (day < 10) {
                day = '0' + day;
            }
            if (hours < 10) {
                hours = '0' + hours;
            }
            if (minutes < 10) {
                minutes = '0' + minutes;
            }

            return `${year}-${month}-${day}T${hours}:${minutes}`;
        }

        function formatDeadline(date) {
            const year = date.getFullYear();
            let month = date.getMonth() + 1;
            let day = date.getDate();
            let hours = date.getHours();
            let minutes = date.getMinutes();

            if (month < 10) {
                month = '0' + month;
            }
            if (day < 10) {
                day = '0' + day;
            }
            if (hours < 10) {
                hours = '0' + hours;
            }
            if (minutes < 10) {
                minutes = '0' + minutes;
            }

            return `${year}-${month}-${day}T${hours}:${minutes}`;
        }

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
.create-news-button {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 180px;
    margin-left: 310px;
    margin-bottom: 10px;
    padding: 10px 20px;
    font-size: 16px;
    font-weight: 600;
    font-family: 'Oxanium', sans-serif;
    letter-spacing: 2px;
    border-radius: 5px;
    border: 1px solid var(--orange);
    color: var(--white);
    background-color: #040B24;
    transition: background-color 0.3s ease, color 0.3s ease;
    
}

.create-news-button:hover {
    background-color: var(--orange);
    color: var(--white);
}

.create-news {
    position: fixed;
    top: 35%;
    left: 50%;
    transform: translate(-50%, -50%);
    font-family: 'Oxanium', sans-serif;
    font-size: 16px;
    min-height: 260px;
    width: 450px;
    background: #111C44;
    color: #fff;
    padding: 20px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    z-index: 1;
}

.create-news button.btn-primary {
    background-color: var(--light-blue);
    color: #fff;
    font-family: 'Oxanium', sans-serif;
    font-weight: 600;
    padding: 5px 50px;
    cursor: pointer;
    border: none;
}

.create-news button.btn-primary:hover {
    background-color: var(--light-blue-hover);
}

.create-news button {
    font-family: 'Oxanium', sans-serif;
    margin: 10px auto;
    width: 100%;
}

.create-news label {
    font-family: 'Oxanium', sans-serif; 
    display: block;
    margin-top: 10px;
}

.create-news input {
    font-family: 'Oxanium', sans-serif;
    width: 100%;
    height: 40px;
    border-radius: 5px;
    outline: none;
}

.create-news textarea {
    font-family: 'Oxanium', sans-serif;
    min-height: 70px;
    width: 100%;
    border-radius: 5px;
    outline: none;
}

.toggle-news {
    position: absolute;
    display: flex;
    justify-content: center;
    align-items: center;
    top: 0;
    right: 0;
    height: 30px;
    width: 30px;
    color: #fff;
    font-size: 25px;
    cursor: pointer;
    transition: 0.3s;
}

.toggle-news:hover {
    background-color: #df3232;
    color: #fff;
    cursor: pointer;
}
</style>
<template>
    <div>
        <div v-if="isFormVisible" class="edit-form">
            <form @submit.prevent="submitForm">
                <label>Subject</label>
                <input v-model="editedNews.subject" type="text" required>

                <label>Message</label>
                <textarea v-model="editedNews.message" required></textarea>

                <button type="submit">Update News</button>
                <button type="button" @click="deleteNewsItem">Delete News</button>
            </form>
        </div>
        <div v-for="newsItem in fetchedNewsList" :key="newsItem.id">
            <div class="news-item">
                <div class="news-content">
                    <h3>{{ newsItem.subject }}</h3>
                    <p>{{ newsItem.message }}</p>
                </div>
                <div class="news-actions">
                    <button @click="editNewsItem(newsItem)">Edit</button>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import { ref, onMounted, watch } from 'vue';
import { useNewsStore } from '@/stores/news';

export default {
    setup() {
        const fetchedNewsList = ref([]);
        const isFormVisible = ref(false);
        const editedNews = ref({});
        const selectedNewsId = ref(null);

        const newsStore = useNewsStore();

        const fetchNewsList = async () => {
            try {
                const response = await newsStore.getAllNews();
                fetchedNewsList.value = response.data || [];
            } catch (error) {
                console.error('Error fetching news list:', error);
            }
        };

        const editNewsItem = (newsItem) => {
            editedNews.value = { ...newsItem };
            selectedNewsId.value = newsItem.id;
            isFormVisible.value = !isFormVisible.value;
        };

        const submitForm = async () => {
            try {
                console.log('Submitting form with updated news:', editedNews.value);

                const response = await newsStore.updateNews(editedNews.value);

                console.log('Update news response:', response);
                await fetchNewsList();
                isFormVisible.value = false;
            } catch (error) {
                console.error('Error updating news:', error);
            }
        };

        const deleteNewsItem = async () => {
            try {
                const newsId = editedNews.value.id;
                console.log('Deleting news with ID:', newsId);
                await newsStore.deleteNews(newsId);
                await fetchNewsList();
                isFormVisible.value = false;
            } catch (error) {
                console.error('Error deleting news:', error);
            }
        };

        onMounted(async () => {
            await fetchNewsList();
            isFormVisible.value = false;
        });

        watch(fetchedNewsList, () => {
            if (fetchedNewsList.value.length > 0) {
                editNewsItem(fetchedNewsList.value[0]);
            }
        });

        return {
            editedNews,
            selectedNewsId,
            fetchedNewsList,
            isFormVisible,
            fetchNewsList,
            editNewsItem,
            submitForm,
            deleteNewsItem,
        };
    },
};
</script>

<style scoped>
.news-item {
    display: flex;
    justify-content: space-between;
    margin-bottom: 10px;
    padding: 10px;
    border: 1px solid #ccc;
}

.news-actions {
    display: flex;
    align-items: center;
}

.news-actions button {
    margin-left: 10px;
}

.edit-form {
    margin-top: 20px;
    position: fixed;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    background-color: #fff;
    padding: 20px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
    z-index: 999;
}

button {
    padding: 10px;
    background-color: #3498db;
    color: #fff;
    border: none;
    cursor: pointer;
}

button:hover {
    background-color: #2980b9;
}
</style>


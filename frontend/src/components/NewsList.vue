<template>
    <div>
        <SearchBarNews @search="handleSearch" />
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
        <div v-for="newsItem in displayedNewsList" :key="newsItem.id">
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
import { ref, onMounted, computed, watch } from 'vue';
import { useNewsStore } from '@/stores/news';
import SearchBarNews from '@/components/SearchBarNews.vue';

export default {
    components: {
        SearchBarNews,
    },

    setup() {
        const newsStore = useNewsStore();
        const isFormVisible = ref(false);
        const editedNews = ref({});
        const selectedNewsId = ref(null);
        const searchQuery = ref('');

        const displayedNewsList = computed(() => {
            return newsStore.newsList
                .slice()
                .sort((a, b) => new Date(b.date) - new Date(a.date))
                .filter(
                    (newsItem) =>
                        newsItem.subject.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
                        newsItem.message.toLowerCase().includes(searchQuery.value.toLowerCase())
                );
        });

        const editNewsItem = (newsItem) => {
            editedNews.value = { ...newsItem };
            selectedNewsId.value = newsItem.id;
            isFormVisible.value = !isFormVisible.value;
        };

        const handleSearch = (query) => {
            searchQuery.value = query;
        };

        onMounted(() => {
            newsStore.getAllNews();
        });

        // Watch for changes in searchQuery and update displayedNewsList accordingly
        watch(searchQuery, () => {
            // You can also fetch updated news based on the search query here if needed
        });

        return {
            editedNews,
            selectedNewsId,
            isFormVisible,
            editNewsItem,
            handleSearch,
            displayedNewsList,
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

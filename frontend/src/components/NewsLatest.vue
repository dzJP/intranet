<template>
    <div>
        <h2>Latest News</h2>
        <div v-if="loading">Fetching latest news...</div>
        <div v-else>
            <div v-if="showAllNews">
                <div v-for="newsItem in newsList" :key="newsItem.id">
                    <h5>{{ newsItem.subject }}</h5>
                    <p>{{ newsItem.message }}</p>
                </div>
            </div>
            <div v-else>
                <div v-if="currentNews">
                    <h5>{{ currentNews.subject }}</h5>
                    <p>{{ currentNews.message }}</p>
                </div>
                <button @click="navigate(-1)" :disabled="currentIndex === 0">Previous</button>
                <button @click="navigate(1)" :disabled="currentIndex === newsList.length - 1">Next</button>
                <button @click="toggleShowAllNews">Show All News</button>
            </div>
        </div>
        <p v-if="!loading && !newsList.length">No news available</p>
    </div>
</template>

<script>
import { defineComponent, onMounted, ref, computed } from 'vue';
import axios from 'axios';
import { useAuthStore } from '@/stores/auth.js';

export default defineComponent({
    setup() {
        const loading = ref(true);
        const newsList = ref([]);
        const currentIndex = ref(0);
        const showAllNews = ref(false);

        const fetchLatestNews = async () => {
            try {
                const auth = useAuthStore();
                const token = auth.token;

                console.log('Fetching latest news...');
                loading.value = true;

                const response = await axios.get('http://localhost:8080/api/v1/get-news', {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                });

                console.log('Latest news response:', response.data);

                newsList.value = response.data;
            } catch (error) {
                console.error('Error fetching latest news:', error);
            } finally {
                loading.value = false;
            }
        };

        const navigate = (step) => {
            const newIndex = currentIndex.value + step;
            if (newIndex >= 0 && newIndex < newsList.value.length) {
                currentIndex.value = newIndex;
            }
        };

        const currentNews = computed(() => {
            return newsList.value[currentIndex.value];
        });

        const toggleShowAllNews = () => {
            showAllNews.value = !showAllNews.value;
        };

        // Call fetchLatestNews when the component is mounted
        onMounted(() => {
            fetchLatestNews();
        });

        // Expose the refs and functions to the template
        return { loading, newsList, currentIndex, navigate, currentNews, showAllNews, toggleShowAllNews };
    },
});
</script>

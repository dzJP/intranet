<template>
    <div class="newslist-card-body">
        <div v-if="newsList && newsList.length > 0">
            <div class="news-cards">
                <div v-for="newsItem in displayedNewsList" :key="newsItem?.id" :class="getNewsItemClass(newsItem)">
                    <div class="news-card">
                        <div class="header">
                            <div class="subject">{{ newsItem ? newsItem.subject || '' : '' }}</div>
                            <div class="date">Date: {{ newsItem ? formatDateTime(newsItem.date) || '' : '' }}</div>
                        </div>
                        <div class="message">{{ newsItem ? newsItem.message || '' : '' }}</div>
                        <div class="deadline">Deadline: {{ newsItem ? formatDateTime(newsItem.deadline) || '' : '' }}</div>
                    </div>
                </div>
            </div>
            <button @click="toggleShowAll">{{ showButtonLabel }}</button>
        </div>
    </div>
</template>


<script>
import { useNewsStore } from '@/stores/news';
import { ref, computed } from 'vue';

export default {
    setup() {
        const newsStore = useNewsStore();
        const showAll = ref(false);

        const getNews = async () => {
            try {
                await newsStore.getAllNews();
            } catch (error) {
                console.error('Error fetching news:', error);
            }
        };

        const displayedNewsList = computed(() => {
            // Sort the news items in descending order based on the creation date
            const sortedNewsList = showAll.value
                ? newsStore.newsList.slice().sort((a, b) => new Date(b.date) - new Date(a.date))
                : newsStore.newsList.slice(0, 5).sort((a, b) => new Date(b.date) - new Date(a.date));

            return sortedNewsList;
        });

        const toggleShowAll = () => {
            showAll.value = !showAll.value;
        };
        const showButtonLabel = computed(() => (showAll.value ? 'Show less' : 'Show more'));

        const formatDateTime = (dateTime) => {
            return dateTime ? new Date(dateTime).toLocaleString() : '';
        };

        const getNewsItemClass = () => {
            return {
                'custom-news-item': true,
            };
        };

        return {
            getNews,
            newsList: newsStore.newsList,
            displayedNewsList,
            toggleShowAll,
            showButton: newsStore.newsList.length > 5,
            formatDateTime,
            getNewsItemClass,
            showButtonLabel
        };
    },
};
</script>

<style scoped>
.news-card {
    background-color: rgb(255, 255, 255);
    border: 1px solid #dadada;
    padding: 20px;
    box-shadow: 0 0 5px rgba(0, 0, 0, 0.1);
    width: 100%;
    margin-bottom: 10px;
    
}

.news-card:hover {
    background-color: #f1f7ff;
}

.header {
    display: flex;
    justify-content: space-between;
}

.subject {
    font-size: 25px;
    font-weight: bold;
    color: #2c3e50;
}

.date {
    color: #7f8c8d;
    font-size: 14px;
}

.message,
.deadline {
    margin-top: 10px;
}

.deadline {
    color: #7f8c8d;
    font-size: 14px;
    text-align: right;
}
</style>
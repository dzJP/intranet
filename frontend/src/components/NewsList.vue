<template>
    <div>
        <button @click="toggleShowAllNews">
            {{ showAllNews ? 'Show less' : 'Show All News' }}
        </button>

        <div v-if="isFormVisible" class="edit-form">
            <form @submit.prevent="submitForm">
                <label>Subject</label>
                <input v-model="editedNews.subject" type="text" required />

                <label>Message</label>
                <textarea v-model="editedNews.message" required></textarea>

                <button type="submit">Update News</button>
                <button type="button" @click="deleteNewsItem">Delete News</button>
                <button type="button" class="close-button" @click="closeForm">Close</button>
            </form>
        </div>
        <div>
            <SearchBarNews :search-query="searchQuery" @search="handleSearch" />
        </div>

        <div v-for="(newsItem, index) in displayedNewsList" :key="newsItem.id">
            <div v-if="showAllNews || index < 5" @click="openPopup(newsItem)" class="news-item"
                :class="{ 'highlighted': isNewsItemHovered === index }" @mouseover="highlightItem(index)"
                @mouseleave="unhighlightItem">
                <div class="news-content">
                    <div class="header">
                        <div class="subject">{{ newsItem.subject }}</div>
                        <div class="date">Date: {{ formatDate(newsItem.date) }}</div>
                    </div>
                    <div>{{ newsItem.message }}</div>
                    <div class="date-time">Deadline: {{ formatDate(newsItem.deadline) }}</div>
                </div>
                <div v-if="allowEdit" class="news-actions">
                    <button @click.stop="editNewsItem(newsItem)">Edit</button>
                </div>
            </div>
        </div>

        <div v-if="selectedNewsItem" class="popup">
            <button @click="closePopup">Close</button>
            <button @click="navigate('previous')">Previous</button>
            <button @click="navigate('next')">Next</button>
            <div class="popup-content">
                <h2>{{ selectedNewsItem.subject }}</h2>
                <p>Date: {{ formatDate(selectedNewsItem.date) }}</p>
                <p>{{ selectedNewsItem.message }}</p>
                <p>Deadline: {{ formatDate(selectedNewsItem.deadline) }}</p>
            </div>
        </div>
    </div>
</template>

<script>
import { ref, onMounted, computed, watch } from 'vue';
import { useNewsStore } from '@/stores/news';
import SearchBarNews from '@/components/SearchBarNews.vue';

export default {
    props: {
        searchQuery: String,
        allowEdit: {
            type: Boolean,
            default: true,
        },
    },
    setup(props) {
        // Reactive variables
        const newsStore = useNewsStore();
        const isFormVisible = ref(false);
        const editedNews = ref({});
        const selectedNewsId = ref(null);
        const searchQuery = ref('');
        const showAllNews = ref(false);
        const selectedNewsItem = ref(null);
        const isNewsItemHovered = ref(null);

        // Computed properties
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

        // Lifecycle hook
        onMounted(() => {
            newsStore.getAllNews();
        });

        // Methods
        const toggleShowAllNews = () => {
            showAllNews.value = !showAllNews.value;
        };

        const formatDate = (dateTime) => new Date(dateTime)?.toLocaleDateString() || '';

        const handleSearch = (query) => {
            searchQuery.value = query;
        };

        const highlightItem = (index) => {
            isNewsItemHovered.value = index;
        };

        const unhighlightItem = () => {
            isNewsItemHovered.value = null;
        };

        const openPopup = (newsItem) => {
            selectedNewsItem.value = newsItem;
        };

        const closePopup = () => {
            selectedNewsItem.value = null;
        };

        const navigate = (direction) => {
            const currentIndex = displayedNewsList.value.findIndex(
                (newsItem) => newsItem.id === selectedNewsItem.value.id
            );

            let newIndex;
            if (direction === 'previous') {
                newIndex = currentIndex > 0 ? currentIndex - 1 : displayedNewsList.value.length - 1;
            } else {
                newIndex = currentIndex < displayedNewsList.value.length - 1 ? currentIndex + 1 : 0;
            }

            selectedNewsItem.value = displayedNewsList.value[newIndex];
        };

        const closeForm = () => {
            isFormVisible.value = false;
        };

        const submitForm = async () => {
            try {
                console.log('Submitting form with updated news:', editedNews.value);

                const response = await newsStore.updateNews(editedNews.value);

                console.log('Update news response:', response);
                await newsStore.getAllNews();
                isFormVisible.value = false;
            } catch (error) {
                console.error('Error updating news:', error);
            }
        };

        const deleteNewsItem = async () => {
            if (selectedNewsId.value) {
                await newsStore.deleteNews(selectedNewsId.value);
                await newsStore.getAllNews();
                isFormVisible.value = false;
            }
        };

        const editNewsItem = (newsItem) => {
            editedNews.value = { ...newsItem };
            selectedNewsId.value = newsItem.id;
            isFormVisible.value = !isFormVisible.value;
        };

        // Watchers
        watch(() => props.searchQuery, (newQuery) => {
            handleSearch(newQuery);
        });

        watch(() => isFormVisible.value, () => {
            if (isFormVisible.value) {
                closePopup();
            }
        });

        // Return data and methods
        return {
            editedNews,
            selectedNewsId,
            isFormVisible,
            editNewsItem,
            handleSearch,
            displayedNewsList,
            showAllNews,
            toggleShowAllNews,
            formatDate,
            isNewsItemHovered,
            highlightItem,
            unhighlightItem,
            openPopup,
            closePopup,
            selectedNewsItem,
            navigate,
            deleteNewsItem,
            submitForm,
            closeForm,
        };
    },
    components: {
        SearchBarNews,
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

.news-item:hover {
    background-color: #f0f0f0;
}

.news-content {
    flex: 1;
}

.header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 8px;
}

.subject {
    font-size: 18px;
    font-weight: bold;
}

.date {
    font-size: 14px;
    color: #777;
}

.date-time {
    font-size: 14px;
    color: #777;
    text-align: right;
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

.popup {
    position: fixed;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    background-color: #fff;
    padding: 20px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
    z-index: 1000;
}

.popup-content {
    padding: 20px;
}

.popup button {
    margin: 5px;
    padding: 10px;
    background-color: #3498db;
    color: #fff;
    border: none;
    cursor: pointer;
}

.popup button:hover {
    background-color: #2980b9;
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

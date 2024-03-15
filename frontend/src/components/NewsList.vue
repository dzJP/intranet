<template>
    <div>
        <button v-if="!hideSearchAndToggleShowAll" class="allnews-btn btn btn-primary" @click="toggleShowAllNews">
            {{ showAllNews ? 'Show less' : 'Show All News' }}
        </button>

        <button type="button" class="oldest-btn btn btn-primary"  @click="toggleSortNewestOldestNews">
            {{ sortNewestOldestNews }}
        </button>

        <div v-if="isEditFormVisible" class="edit-form">
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

        <div v-if="!hideSearchAndToggleShowAll">
            <SearchBarNews 
            class="search-bar"
            :search-query="searchQuery" @search="handleSearch" />
        </div>

        <div v-for="(newsItem, index) in displayedNewsList" :key="newsItem.id">
            <div v-if="showAllNews || index < 5" @click="openNewsDetailsPopup(newsItem)" class="news-item"
                :class="{ 'highlighted': isNewsItemHovered === index }" @mouseover="highlightItem(index)"
                @mouseleave="unhighlightItem">
                <div class="news-content">
                    <div class="header">
                        <div class="subject">{{ newsItem.subject }}</div>
                        <div class="date">Date: {{ formatDate(newsItem.date) }}</div>
                    </div>
                    <div>{{ newsItem.message }}</div>
                    <div v-if="newsItem.deadline" class="date-time">Deadline: {{ formatDate(newsItem.deadline) }}</div>
                </div>
                <div v-if="allowEdit" class="news-actions">
                    <button @click.stop="editNewsItem(newsItem)">Edit</button>
                </div>
            </div>
        </div>

        <div v-if="selectedNewsItem" class="popup">
            <button @click="closeNewsDetailsPopup">Close</button>
            <button @click="navigate('previous')">Previous</button>
            <button @click="navigate('next')">Next</button>
            <div class="popup-content">
                <h2>{{ selectedNewsItem.subject }}</h2>
                <p>Date: {{ formatDate(selectedNewsItem.date) }}</p>
                <p>{{ selectedNewsItem.message }}</p>
                <p>Deadline: {{ formatDate(selectedNewsItem.deadline) }}</p>
                <button @click="openSharePopup">Share News</button>
            </div>
        </div>
        <div v-if="showSharePopup" class="popup">
            <button @click="closeSharePopup">Close</button>
            <div class="popup-content">
                <h2>Share News</h2>
                <textarea v-model="shareText" placeholder="Enter email.."></textarea>
                <button @click="shareNews">Submit</button>
            </div>
        </div>
    </div>
</template>

<script>
import { ref, onMounted, computed, watch } from 'vue';
import { useNewsStore } from '@/stores/news';
import SearchBarNews from '@/components/SearchBarNews.vue';
import axios from 'axios';

export default {
    props: {
        searchQuery: String,
        maxEntries: {
            type: Number,
            default: null,
        },
        allowEdit: {
            type: Boolean,
            default: true,
        },
        hideSearchAndToggleShowAll: {
            type: Boolean,
            default: false,
        }
    },
    setup(props) {
        const newsStore = useNewsStore();

        const editedNews = ref({});
        const selectedNewsId = ref(null);
        const selectedNewsItem = ref(null);
        const isNewsItemHovered = ref(null);
        const searchQuery = ref('');
        const shareText = ref('');
        const showAllNews = ref(false);
        const isEditFormVisible = ref(false);
        const ascendingOrder = ref(false);
        const showSharePopup = ref(false);

        const sortNewestOldestNews = computed(() => (ascendingOrder.value ? 'Show newest first' : 'Show oldest first'));

        const displayedNewsList = computed(() => {
            const sortedNewsList = newsStore.newsList
                .slice()
                .sort((a, b) =>
                    ascendingOrder.value
                        ? new Date(a.date) - new Date(b.date)
                        : new Date(b.date) - new Date(a.date)
                );

            const filteredList = sortedNewsList.filter((newsItem) =>
                newsItem.subject.toLowerCase().includes(searchQuery.value.toLowerCase())
            );

            return props.maxEntries ? filteredList.slice(0, props.maxEntries) : filteredList;
        });

        onMounted(() => {
            newsStore.getAllNews();
        });

        const toggleShowAllNews = () => {
            showAllNews.value = !showAllNews.value;
        };

        const toggleSortNewestOldestNews = () => {
            ascendingOrder.value = !ascendingOrder.value;
        };

        const isSearchVisible = computed(() => !props.hideSearch);
        const formatDate = (dateTime) => {
            if (!dateTime) {
                return '';
            }
            return new Date(dateTime).toLocaleDateString() || '';
        };

        const handleSearch = (query) => {
            searchQuery.value = query;
        };

        const highlightItem = (index) => {
            isNewsItemHovered.value = index;
        };

        const unhighlightItem = () => {
            isNewsItemHovered.value = null;
        };

        const openNewsDetailsPopup = (newsItem) => {
            selectedNewsItem.value = newsItem;
        };

        const closeNewsDetailsPopup = () => {
            selectedNewsItem.value = null;
        };

        const navigate = (direction) => {
            const currentIndex = newsStore.newsList.findIndex(
                (newsItem) => newsItem.id === selectedNewsItem.value.id
            );

            let newIndex;
            if (direction === 'previous') {
                newIndex = currentIndex > 0 ? currentIndex - 1 : newsStore.newsList.length - 1;
            } else {
                newIndex = currentIndex < newsStore.newsList.length - 1 ? currentIndex + 1 : 0;
            }

            selectedNewsItem.value = newsStore.newsList[newIndex];
        };

        const closeForm = () => {
            isEditFormVisible.value = false;
        };

        const submitForm = async () => {
            try {
                console.log('Submitting form with updated news:', editedNews.value);

                const response = await newsStore.updateNews(editedNews.value);

                console.log('Update news response:', response);
                await newsStore.getAllNews();
                isEditFormVisible.value = false;
            } catch (error) {
                console.error('Error updating news:', error);
            }
        };

        const deleteNewsItem = async () => {
            if (selectedNewsId.value) {
                await newsStore.deleteNews(selectedNewsId.value);
                await newsStore.getAllNews();
                isEditFormVisible.value = false;
            }
        };

        const editNewsItem = (newsItem) => {
            editedNews.value = { ...newsItem };
            selectedNewsId.value = newsItem.id;
            isEditFormVisible.value = !isEditFormVisible.value;
        };

        const openSharePopup = () => {
            showSharePopup.value = true;
        };

        const closeSharePopup = () => {
            showSharePopup.value = false;
        };

        const shareNews = async () => {
            const jwtToken = localStorage.getItem('token');
            if (!jwtToken) {
                console.error('JWT token not found in local storage.');
                return;
            }

            const requestBody = {
                email: shareText.value,
                newsDTO: selectedNewsItem.value
            };

            console.log('Request Payload:', requestBody);

            try {
                const response = await axios.post('http://localhost:8080/api/v1/admin/share-news', requestBody, {
                    headers: {
                        Authorization: `Bearer ${jwtToken}`
                    }
                });

                console.log('Response from server:', response.data);
                closeSharePopup();
            } catch (error) {
                console.error('Error sharing news:', error.response ? error.response.data : error.message);
            }
        };

        watch(() => props.searchQuery, (newQuery) => {
            handleSearch(newQuery);
        });

        watch(() => isEditFormVisible.value, () => {
            if (isEditFormVisible.value) {
                closeNewsDetailsPopup();
            }
        });

        return {
            editedNews,
            selectedNewsId,
            isEditFormVisible,
            editNewsItem,
            handleSearch,
            isSearchVisible,
            displayedNewsList,
            showAllNews,
            toggleShowAllNews,
            toggleSortNewestOldestNews,
            formatDate,
            isNewsItemHovered,
            highlightItem,
            unhighlightItem,
            openNewsDetailsPopup,
            closeNewsDetailsPopup,
            selectedNewsItem,
            navigate,
            deleteNewsItem,
            submitForm,
            closeForm,
            sortNewestOldestNews,
            showSharePopup,
            shareText,
            openSharePopup,
            closeSharePopup,
            shareNews,
        };
    },
    components: {
        SearchBarNews,
    },
};
</script>


<style scoped>

.allnews-btn {
    margin-left: 310px;
}

.search-bar {
    display: flex;
    width: 600px;
    margin: auto;
    margin-bottom: 20px;
    padding: 10px;
    border-radius: 5px;
    outline: none;
    border: 1px solid #ccc;
}

.news-item {
    display: flex;
    justify-content: space-between;
    margin-bottom: 10px;
    padding: 10px;
    border: 1px solid #ccc;
    width: 980px;
    margin: auto;
    border-radius: 9px;
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
    color: #fff;
    border: none;
    cursor: pointer;
}

button:hover {
    background-color: #2980b9;
}
</style>

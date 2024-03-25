<template>
    <div>

        <button type="button" class="show-oldest-news-button" @click="toggleSortNewestOldestNews">
            {{ sortNewestOldestNews }}
        </button>

        <button v-if="!hideSearchAndToggleShowAll" class="show-all-news-button" @click="toggleShowAllNews">
            {{ showAllNews ? 'Show less' : 'Show all' }}
        </button>

        <div v-if="isEditFormVisible" class="edit-form">
            <form @submit.prevent="submitForm">
                <input v-model="editedNews.subject" type="text" required />

                <textarea v-model="editedNews.message" required></textarea>
                <div class="button-container-edit-news">
                <button type="submit" class="btn">Update News</button>
                <button type="button" @click="deleteNewsItem" class="btn">Delete News</button>
                <button type="button" class="btn close-button" @click="closeForm">Close</button>
            </div>
            </form>
        </div>

        <div v-if="!hideSearchAndToggleShowAll">
            <SearchBarNews class="search-bar" :search-query="searchQuery" @search="handleSearch" />
        </div>

        <div v-for="(newsItem, index) in displayedNewsList" :key="newsItem.id">
            <div v-if="showAllNews || index < 5" @click="openNewsDetailsPopup(newsItem)" class="news-item"
                :class="{ 'highlighted': isNewsItemHovered === index }" @mouseover="highlightItem(index)"
                @mouseleave="unhighlightItem">
                <div class="news-content">
                    <div class="header">
                        <div class="news-item-subject">{{ newsItem.subject }}</div>
                        <div class="date">Date: {{ formatDate(newsItem.date) }}</div>
                    </div>
                    <div class="news-item-message">{{ newsItem.message }}</div>
                    <div v-if="newsItem.deadline" class="deadline">Deadline: {{ formatDate(newsItem.deadline) }}</div>
                </div>
                <div v-if="allowEdit" class="edit-news-button">
                    <button class="btn edit-news-button-button" @click.stop="editNewsItem(newsItem)">Edit</button>
                </div>
            </div>
        </div>

        <div v-if="selectedNewsItem" class="news-item-popup">
            <div class="news-item-popup-content">
                <div class="news-item-popup-subject">{{ selectedNewsItem.subject }}</div>
                <div class="news-item-popup-date">Date: {{ formatDate(selectedNewsItem.date) }}</div>
                <div class="news-item-popup-message">{{ selectedNewsItem.message }}</div>
                <div class="news-item-popup-deadline">Deadline: {{ formatDate(selectedNewsItem.deadline) }}</div>
            </div>
            <div class="button-container">
                <button @click="navigate('previous')" class="btn">Previous</button>
                <button @click="navigate('next')" class="btn">Next</button>
                <button @click="closeNewsDetailsPopup" class="btn">Close</button>
                <button @click="openSharePopup" class="btn">Share</button>
            </div>
        </div>
        <div v-if="showSharePopup" class="share-news-popup">
            <div class="share-news-popup-content">
                <h3>Share news</h3>
                <textarea v-model="shareText" placeholder="Enter email.."></textarea>
            </div>
            <div class="button-container">
                <button @click="closeSharePopup" class="btn close-button">Close</button>
                <button @click="shareNews" class="btn submit-button">Submit</button>
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

        const sortNewestOldestNews = computed(() => (ascendingOrder.value ? 'SHOW LATEST' : 'SHOW OLDEST'));

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

.show-oldest-news-button, .show-all-news-button,
.button-container button {
    letter-spacing: 2px;
    text-transform: uppercase;
    transition: background-color 0.3s ease, color 0.3s ease;
    font-family: 'Oxanium', sans-serif;
    margin-bottom: 10px;
}

.show-oldest-news-button {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 180px;
    margin-left: 310px;
    padding: 10px 20px;
    font-size: 16px;
    font-weight: 600;
    border-radius: 5px;
    border: 1px solid var(--orange);
    color: var(--white);
    background-color: #040B24;
    transition: background-color 0.3s ease, color 0.3s ease;
}

.show-all-news-button {
    display: flex;
    justify-content: center;
    align-items: center;
    margin-left: 310px;
    width: 180px;
    padding: 10px 35px;
    font-size: 16px;
    font-weight: 600;
    border-radius: 5px;
    border: 1px solid var(--orange);
    color: var(--white);
    background-color: #040B24;
}

.show-all-news-button:hover,
.show-oldest-news-button:hover {
    background-color: var(--orange);
}

.search-bar {
    background-color: #040B24;
    color: #fff;
    display: flex;
    width: 600px;
    margin: auto;
    margin-bottom: 20px;
    padding: 10px;
    border: 2px solid #111C44;
    font-family: 'Oxanium', sans-serif;
}

.news-item {
    display: flex;
    justify-content: space-between;
    width: 600px;
    height: 175px;
    color: #ffffffd3;
    margin-bottom: 10px;
    padding: 10px;
    border: 2px solid #111C44;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    font-family: 'Oxanium', sans-serif;
    margin: auto;
}

.news-item:hover {
    background-color: #111c446c;
}

.edit-news-button-button {
    border: none;
  color: #fff;
  font-size: 14px;
  font-weight: 600;
  text-transform: uppercase;
  margin-right: 10px;
  cursor: pointer;
  font-family: 'Oxanium', sans-serif;
  transition: background-color 0.3s ease, color 0.3s ease;
  background-color: var(--light-blue);
}

.news-item-subject {
    color: #ffffff;
    font-size: 20px;
    font-weight: bold;
    font-family: 'Oxanium', sans-serif;
    margin-left: 2px;
}

.news-item-message {
    color: #ffffffd5;
    font-size: 16px;
    word-wrap: break-word;
    overflow: hidden;
    text-overflow: ellipsis;
    font-family: 'Oxanium', sans-serif;
    max-height: 6em;
    line-height: 1.5em;
    margin-top: 2px;
    margin-left: 5px;
}

.date {
    font-size: 12px;
    color: #ffffff;
    font-family: 'Oxanium', sans-serif;
    position: absolute;
    bottom: 0;
    left: 0;
    margin-left: 5px;
}

.deadline {
    font-size: 12px;
    color: #ffffff;
    font-family: 'Oxanium', sans-serif;
    position: absolute;
    bottom: 0;
    left: 450px;
    width: 100%;
}

.news-item-popup {
    position: fixed;
    top: 50%;
    left: 50%;
    width: 500px;
    height: 400px;
    background-color: var(--dark-blue);
    transform: translate(-50%, -50%);
    border: 2px solid #111C44;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
    z-index: 1000;
}

.news-item-popup-subject {
    font-size: 22px;
    color: #ffffff;
    font-family: 'Oxanium', sans-serif;
    text-align: center;
    font-weight: 600;
    margin-top: 30px;
}

.news-item-popup-message {
    font-size: 16;
    color: #ffffff;
    font-family: 'Oxanium', sans-serif;
    text-align: center;
}

.news-item-popup-date {
    position: absolute;
    left: 75%;
    top: 0;
    margin-top: 10px;
    margin-left: 10px;
    font-size: 12px;
    color: #ffffff;
    font-family: 'Oxanium', sans-serif;
}

.news-item-popup-deadline {
    position: absolute;
    bottom: 15%;
    text-align: center;
    font-size: 12px;
    color: #ffffff;
    font-family: 'Oxanium', sans-serif;
    width: 100%;
}

.news-content {
    flex: 1;
    position: relative;
    font-family: 'Oxanium', sans-serif;
    width: 300px;
}

.share-news-popup {
    position: fixed;
    top: 50%;
    left: 50%;
    width: 400px;
    height: auto;
    background-color: var(--dark-blue);
    transform: translate(-50%, -50%);
    border-radius: 5px;
    border: 2px solid #111C44;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
    z-index: 1000;
}

.share-news-popup-content textarea {
    width: 75%;
    height: 50px;
    margin-top: 60px;
    margin-left: 50px;
    font-size: 22px;
    color: #000000;
    border-radius: 5px;
    font-family: 'Oxanium', sans-serif;
}

.submit-button,
.close-button {
    background-color: var(--light-blue);
}

.submit-button:hover,
.close-button:hover {
    background-color: var(--light-blue-hover);
}

.share-news-popup-content {
    font-family: 'Oxanium', sans-serif;
    top: 50%;
    left: 50%;
    height: 300px;
    background-color: var(--dark-blue);
}

.edit-news-button-button {
    display: flex;
    text-align: center;
    justify-content: center;
    height: 36px;
    font-size: 14px;
    text-transform: uppercase;
    cursor: pointer;
    font-family: 'Oxanium', sans-serif;
    transition: background-color 0.3s ease, color 0.3s ease;
    box-shadow: 1px 1px 8px 1px #2525A5;
    background-color: var(--light-blue);
}

.edit-news-button-button:hover {
    background-color: var(--light-blue-hover);
}

.edit-form {
    position: fixed;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    background-color: var(--dark-blue);
    border: 2px solid #111C44;
    padding: 20px;
    border-radius: 5px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
    z-index: 1000;
}

.edit-form textarea {
    width: 100%;
    height: 200px;
    padding: 10px;
    margin-bottom: 15px;
    margin-top: 10px;
    border: 2px solid #111C44;
    border-radius: 5px;
    background-color: var(--dark-blue);
    color: #fff;
    font-family: 'Oxanium', sans-serif;
    font-size: 16px;
}

.edit-form input[type="text"] {
    width: 100%;
    padding: 10px;
    color: #fff;
    background-color: var(--dark-blue);
    border: 2px solid #111C44;
    font-family: 'Oxanium', sans-serif;
    font-size: 16px;
}
.button-container-edit-news {
    margin-left: 20px;
}

.edit-form button {
    padding: 10px;
    border: none;
    border-radius: 5px;
    background-color: var(--light-blue);
    color: #fff;
    font-family: 'Oxanium', sans-serif;
    font-size: 14px;
    font-weight: 600;
    cursor: pointer;
    transition: background-color 0.3s ease;
}

.edit-form button:hover {
    background-color: rgba(41, 41, 192, 0.507);
}

h3 {
    margin-top: 15px;
    margin-left: 125px;
    font-weight: 600;
    position: absolute;
    color: #ffffff;
    font-size: 22px;
    font-family: 'Oxanium', sans-serif;
    text-transform: uppercase;
}

.share-news-popup-content button:hover {
    background-color: #2d2dc7;
}

h2,
p {
    color: #fff;
    font-family: 'Oxanium', sans-serif;
    margin-top: 10px;
    margin-right: 10px;

}

.popup button {
    background-color: var(--light-blue);
    color: #fff;
    font-size: 16px;
    font-family: 'Oxanium', sans-serif;
    margin: 5px;
    padding: 10px;
    border-radius: 5px;
    cursor: pointer;
}

.popup button:hover {
    background-color: rgba(41, 41, 192, 0.507);
}

.button-container {
    position: absolute;
    bottom: 10px;
    left: 50%;
    transform: translateX(-50%);
    display: flex;
    align-items: center;
}

.button-container button {
    border: none;
  color: #fff;
  font-size: 14px;
  font-weight: 600;
  text-transform: uppercase;
  margin-right: 10px;
  cursor: pointer;
  font-family: 'Oxanium', sans-serif;
  transition: background-color 0.3s ease, color 0.3s ease;
  background-color: var(--light-blue);
}

.button-container button:hover {
    background-color: var(--light-blue-hover);
}
</style>
<template>
    <div class="card news-list">
        <div class="card-body px-0 pt-0 pb-2">
            <div class="table-responsive p-0">
                <table class="table mb-0">
                    <thead>
                        <tr>
                            <th class="text-uppercase text-secondary text-xxs font-weight-bolder opacity-7">Subject</th>
                            <th class="text-uppercase text-secondary text-xxs font-weight-bolder opacity-7">Message</th>
                            <th class="text-center text-uppercase text-secondary text-xxs font-weight-bolder opacity-7">
                                Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <template v-if="showAllNews">
                            <tr v-if="newsList.length === 0">
                                <td colspan="3">No news available</td>
                            </tr>
                            <tr v-else v-for="newsItem in newsList" :key="newsItem.id">
                                <td>{{ newsItem.subject }}</td>
                                <td>{{ newsItem.message }}</td>
                                <td>
                                    <button class="btn btn-edit" @click="editNews(newsItem)">Edit</button>
                                    <button class="btn btn-delete" @click="deleteNews(newsItem)">Delete (ID: {{ newsItem.id
                                    }})</button>
                                </td>
                            </tr>
                        </template>
                        <template v-else>
                            <tr v-if="currentNews">
                                <td>{{ currentNews.subject }}</td>
                                <td>{{ currentNews.message }}</td>
                                <td>
                                    <button class="btn btn-edit" @click="editNews(currentNews)">Edit</button>
                                    <button class="btn btn-delete" @click="deleteNews(currentNews)">Delete (ID: {{
                                        currentNews.id }})</button>
                                </td>
                            </tr>
                            <tr v-else>
                                <td colspan="3">No news available</td>
                            </tr>
                        </template>
                    </tbody>
                </table>
            </div>
        </div>

        <div class="card-body news px-0 pt-0 pb-2">
            <button @click="navigate(-1)" :disabled="currentIndex === 0">Previous</button>
            <button @click="navigate(1)" :disabled="currentIndex === newsList.length - 1">Next</button>
            <button @click="toggleShowAllNews">{{ showAllNews ? 'View latest news' : 'View all news' }}</button>
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
    </div>

    <div v-if="isEditing" class="edit-window">
        <form @submit.prevent="updateNews">
            <label>Subject</label>
            <input v-model="editingNews.subject" type="text" required>
            <label>Message</label>
            <textarea v-model="editingNews.message" required></textarea>
            <button type="submit" class="btn btn-primary">Update News</button>
            <button @click="cancelEdit" class="btn btn-secondary">Cancel</button>
        </form>
    </div>
</template>

<script>
import { onMounted, ref } from 'vue';
import { useNewsStore } from '@/stores/news';

export default {
    setup() {
        const newsStore = useNewsStore();
        const currentNews = ref(null);
        const newNews = ref({ subject: '', message: '' });
        const editingNews = ref({ id: null, subject: '', message: '', date: '', deadline: '' });
        const isEditing = ref(false);
        const currentIndex = ref(0);
        const newsList = ref([]);
        const showAllNews = ref(false);

        const getNews = async () => {
            try {
                const response = await newsStore.getAllNews();
                console.log('Fetched news data:', response.data);

                // Display the first news article if available
                newsList.value = response.data.map(news => ({ ...news, id: news.id || null }));
                currentIndex.value = 0;
                currentNews.value = newsList.value.length > 0 ? { ...newsList.value[0] } : null;
            } catch (error) {
                console.error('Error fetching news:', error);
            }
        };

        const createNews = async () => {
            try {
                const createdNews = await newsStore.createNews(newNews.value);

                console.log("text", createdNews.data);
                newNews.value = { subject: '', message: '', id: createdNews?.id || null };
                getNews();
            } catch (error) {
                console.error('Error creating news:', error);
            }
        };

        const deleteNews = async (news) => {
            console.log(news.id);
            try {
                const newsId = news.id;

                console.log('Deleting news with ID:', newsId);
                await newsStore.deleteNews(newsId);
                getNews();
            } catch (error) {
                console.error('Error deleting news:', error);
            }
        };

        const editNews = (news) => {
            editingNews.value = { ...news };
            isEditing.value = true;
        };

        const updateNews = async () => {
            try {
                await newsStore.updateNews(editingNews.value);
                isEditing.value = false;
                getNews();
            } catch (error) {
                console.error('Error updating news:', error);
            }
        };

        const cancelEdit = () => {
            isEditing.value = false;
        };

        const navigate = (step) => {
            console.log('Before Navigation - currentIndex:', currentIndex.value, 'newsList:', [...newsList.value]);

            const newIndex = currentIndex.value + step;

            if (newIndex >= 0 && newIndex < newsList.value.length) {
                currentIndex.value = newIndex;
                currentNews.value = { ...newsList.value[newIndex], id: newsList.value[newIndex].id || null };
            }

            console.log('After Navigation - currentIndex:', currentIndex.value, 'newsList:', [...newsList.value]);
        };

        const toggleShowAllNews = () => {
            showAllNews.value = !showAllNews.value;
        };

        onMounted(async () => {
            await getNews(); // Await for the news data to be fetched
            currentIndex.value = 0; // Set the currentIndex after news data is available
        });

        return {
            newsList,
            currentNews,
            newNews,
            createNews,
            deleteNews,
            editingNews,
            isEditing,
            editNews,
            updateNews,
            cancelEdit,
            navigate,
            toggleShowAllNews,
            showAllNews,
            getNews,
            currentIndex
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

.edit-window {
    margin-top: 20px;
    padding: 10px;
    border: 1px solid #ccc;
}
</style>
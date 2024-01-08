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
                                Actions
                            </th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="news in newsList" :key="news.id">
                            <td>{{ news.subject }}</td>
                            <td>{{ news.message }}</td>
                            <td>
                                <button class="btn btn-edit" @click="editNews(news)">Edit</button>
                                <button class="btn btn-delete" @click="deleteNews(news)">Delete (ID: {{ news.id }})</button>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>

        <div class="card-body px-0 pt-0 pb-2">
            <form @submit.prevent="createNews">
                <label>Subject</label>
                <input v-model="newNews.subject" type="text" required>
                <label>Message</label>
                <textarea v-model="newNews.message" required></textarea>
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
        const newsList = ref([]);
        const newNews = ref({ subject: '', message: '' });
        const editingNews = ref({ id: null, subject: '', message: '' });
        const isEditing = ref(false);

        const getNews = async () => {
            try {
                const response = await newsStore.getAllNews();
                console.log('Fetched news data:', response.data);
                newsList.value = response.data.map(news => ({ ...news, id: news.id || null }));
            } catch (error) {
                console.error('Error fetching news:', error);
            }
        };

        const createNews = async () => {
            try {
                const createdNews = await newsStore.createNews(newNews.value);

                console.log("text", createdNews.data);
                // Ensure that the id property is set for the newly created news item
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

        onMounted(() => {
            getNews();
        });
        console.log("asd", newsList);

        return {
            newsList,
            newNews,
            createNews,
            deleteNews,
            editingNews,
            isEditing,
            editNews,
            updateNews,
            cancelEdit,
        };
    },
};
</script>

<style scoped>
.edit-window {
    margin-top: 20px;
    padding: 10px;
    border: 1px solid #ccc;
}
</style>
import { useAuthStore } from "@/stores/auth";
import axios from 'axios';

export async function createNews(news) {
    try {
        const auth = useAuthStore();
        const token = auth.token;

        const fetchOptions = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`,
            },
            body: JSON.stringify(news),
        };

        const response = await fetch('http://localhost:8080/api/v1/create-news', fetchOptions);

        if (response.ok) {
            if (response.headers.get('content-type')?.includes('application/json')) {
                return await response.json();
            } else {
                console.warn('Received non-JSON response:', response);
            }
        } else {
            const errorText = await response.text();
            throw new Error(`Error creating news article: ${response.status} ${response.statusText} ${errorText}`);
        }
    } catch (error) {
        console.error('Error creating news article:', error);
        throw error;
    }
}
export async function fetchNews() {
    try {
        const auth = useAuthStore();
        const token = auth.token;

        const response = await fetch("http://localhost:8080/api/v1/get-news", {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        });

        if (response.ok) {
            return await response.json();
        } else {
            console.error("Error fetching news articles. Status:", response.status);
            throw new Error(`Error fetching news articles. Status: ${response.status}`);
        }
    } catch (error) {
        console.error("Error fetching news articles:", error);
        throw error;
    }
}
export async function fetchLatestNews() {
    try {
        const auth = useAuthStore();
        const token = auth.token;

        console.log('Fetching latest news...');

        const response = await axios.get('http://localhost:8080/api/v1/get-news', {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        });

        console.log('Latest news response:', response.data);

        return response.data;
    } catch (error) {
        console.error('Error fetching latest news:', error);
        throw error;
    }
}

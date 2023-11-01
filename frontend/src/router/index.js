import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import LoginView from '../views/LoginView.vue'
import { useAuthStore } from '@/stores/auth'

const routes = [
	{
		path: '/',
		name: 'home',
		component: HomeView
	},
	{
		path: '/login',
		name: 'login',
		component: LoginView
	},
	{
		path: '/profile',
		name: 'profile',
		component: () => import('../views/ProfileView.vue')
	},
	{
		path: '/colleagues',
		name: 'colleagues',
		component: () => import('../views/ColleaguesView.vue')
	},
	{
		path: '/register',
		name: 'register',
		component: () => import('../views/RegisterView.vue')
	},
	{
		path: '/admin',
		name: 'admin',
		component: () => import('../views/AdminView.vue')
	}
]

const router = createRouter({
	history: createWebHistory(process.env.BASE_URL),
	routes
})
router.beforeEach(async (to) => {

	// Define an array of public page paths that don't require authentication
	const publicPages = ['/login', '/register'];

	// Determine if the page being accessed requires authentication
	const authRequired = !publicPages.includes(to.path);

	// Access the authentication store to get user information
	const auth = useAuthStore();

	// If authentication is required and the user is not logged in
	if (authRequired && !auth.user) {
		// Store the current URL as the return URL for after login
		auth.returnUrl = to.fullPath;
		// Redirect to the login page
		return '/login';
	}

	// If the user is trying to access the admin page but is not an admin
	if (to.path === '/admin' && auth.role !== 'ROLE_ADMIN') {
		console.error('User does not have access to admin panel.');

		// Redirect to a different page if the user doesn't have the required role
		return '/';
	}
});

export default router

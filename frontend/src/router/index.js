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
	const publicPages = ['/login', '/register']
	const authRequired = !publicPages.includes(to.path)
	const auth = useAuthStore();

	// if auth is required and the user is not logged in
	if (authRequired && !auth.user) {
		auth.returnUrl = to.fullPath;
		return '/login';
	}
});

export default router

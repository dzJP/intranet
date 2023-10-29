<script>
import { reactive } from 'vue';
import { useAuthStore } from '@/stores/auth';

const user = reactive({
	email: '',
	password: '',
});

function onSubmit() {
	console.log(user);
	if (user.email !== '' && user.password !== '') {
		useAuthStore().login(user.email, user.password);
	}
}

export default {
	name: 'LoginForm',
	setup() {
		return {
			user,
			onSubmit,
		};
	},
};

</script>

<template>
	<div class="container">
		<div class="login-form mx-auto">
			<form @submit.prevent="onSubmit">
				<div class="form-group mb-3">
					<label for="email">Email</label>
					<input type="email" class="form-control" id="email" v-model="user.email" />
				</div>
				<div class="form-group mb-3">
					<label for="password">Lösenord</label>
					<input type="password" class="form-control" id="password" v-model="user.password" />
				</div>
				<button type="submit" class="btn btn-primary">Logga in</button>
			</form>
		</div>
	</div>
</template>

<style scoped>
.login-form {
	width: 500px;
}
</style>
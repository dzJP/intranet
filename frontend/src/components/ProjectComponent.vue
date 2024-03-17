<template>
  <button @click="togglePopup" class="viewproject-btn btn btn-primary">View Projects</button>

  <div v-if="isPopupVisible" class="project-popup">
    <div class="popup-content">
      <i class="toggle bi bi-x" @click="togglePopup"></i>
      <h2>Projects</h2>

      <ul class="project-list">
        <li v-for="project in projects" :key="project.id">
          {{ project.project }}
          <button @click="() => showUpdateForm(project)" class="btn btn-edit">Edit</button>
          <button v-if="project.active" @click="() => inactivateProject(project.id)" class="btn btn-edit">Inactivate</button>
          <button v-else @click="() => activeProjects(project.id)" class="btn btn-primary">Activate</button>
          <button @click="() => deleteProject(project.id)" class="btn btn-delete">Delete</button>
        </li>
      </ul>

      <div>
        <button @click="showCreateForm" class="btn btn-success create-btn">Create New Project</button>
      </div>

      <form v-if="isCreateFormVisible" @submit.prevent="createNewProject">
          <label for="newProjectName">New Project Name:</label>
          <input v-model="newProjectName" type="text" id="newProjectName" required />
          <button type="submit" class="btn btn-primary">Create</button>
          <button @click="hideCreateForm" type="button" class="btn btn-secondary">Cancel</button>
        </form>

        <form v-if="isUpdateFormVisible" @submit.prevent="updateExistingProject">
          <label for="updatedProjectName">Updated Project Name:</label>
          <input v-model="updatedProjectName" type="text" id="updatedProjectName" required />
          <button type="submit" class="btn btn-primary">Update</button>
          <button @click="hideUpdateForm" type="button" class="btn btn-secondary">Cancel</button>
        </form>

    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useProjectStore } from '../stores/project';

const { getAllProjects, createProject, updateProject } = useProjectStore();

const projects = ref([]);
const newProjectName = ref('');
const isPopupVisible = ref(false);
const isCreateFormVisible = ref(false);
const isUpdateFormVisible = ref(false);
const updatedProjectName = ref('');
let updatingProjectId = ref(null);


const togglePopup = () => {
  isPopupVisible.value = !isPopupVisible.value;
};

const createNewProject = async () => {
  try {
    await createProject({ project: newProjectName.value });
    hideCreateForm();
    projects.value = await getAllProjects();
  } catch (error) {
    console.error('Error creating project:', error);
  }
};

const showCreateForm = () => {
  isCreateFormVisible.value = true;
};

const hideCreateForm = () => {
  isCreateFormVisible.value = false;
  newProjectName.value = '';
};

const showUpdateForm = (project) => {
  isUpdateFormVisible.value = true;
  updatedProjectName.value = project.project;
  updatingProjectId = project.id;
};

const hideUpdateForm = () => {
  isUpdateFormVisible.value = false;
  updatedProjectName.value = '';
  updatingProjectId = null;
};


const updateExistingProject = async () => {
  try {
    const projectDTO = {
      id: updatingProjectId,
      project: updatedProjectName.value,
    };

    await updateProject(projectDTO);
    hideUpdateForm();
    projects.value = await getAllProjects();
  } catch (error) {
    console.error('Error updating project:', error);
  }
};

const inactivateProject = async (id) => {
  try {
    await useProjectStore().inactivateProject(id);
    projects.value = await getAllProjects();
  } catch (error) {
    console.error('Error inactivating project:', error);
  }
};

const activeProjects = async (id) => {
  try {
    await useProjectStore().activateProject(id);
    projects.value = await getAllProjects();
  } catch (error) {
    console.error('Error activating project', error);
  }
}

const deleteProject = async (id) => {
  try {
    await useProjectStore().deleteProject(id);
    projects.value = await getAllProjects();
  } catch (error) {
    console.error('Error deleting project:', error);
  }
};

onMounted(async () => {
  try {
    projects.value = await getAllProjects();
  } catch (error) {
    console.error('Error fetching projects:', error);
  }
});

</script>

<style>

.viewproject-btn {
  margin-top: 50px;
  margin-left: 310px;
}

.project-popup {
  position: fixed;
  top: 35%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 600px;
  font-size: 16px;
  min-height: 400px;
  background: #fff;
  padding: 20px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  z-index: 1;
}

.create-btn {
  position: absolute;
  bottom: 0;
  right: 10px;
}

.btn-delete,
.btn-edit {
  color: #454444;
  border-color: #454444;
  border: none;
}

.btn-edit:hover {
  color: #fff;
  background-color: #454444;
  border-color: #454444;
}

.btn-delete:hover {
  color: #fff;
  background-color: #df3232;
  border-color: #f13d3d;
}

.project-list {
  list-style: none;
  padding: 0;
}

</style>
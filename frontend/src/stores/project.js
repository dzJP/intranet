import { defineStore } from "pinia";
import axios from "axios";

export const useProjectStore = defineStore({
  id: "project",
  state: () => ({
    projects: [],
    selectedProject: null,
  }),
  actions: {
    async getAllProjects() {
      try {
        const response = await axios.get('http://localhost:8080/api/v1/projects');
        this.projects = response.data;
        return response.data;
      } catch (error) {
        console.error('Error fetching all projects:', error);
        throw error;
      }
    },

    async createProject(projectDTO) {
      try {
        const response = await axios.post('http://localhost:8080/api/v1/projects', projectDTO);
        this.projects.push(response.data);
        return response.data;
      } catch (error) {
        console.error('Error creating project:', error);
        throw error;
      }
    },

    async getProjectById(id) {
      try {
        const response = await axios.get(`http://localhost:8080/api/v1/projects/${id}`);
        this.selectedProject = response.data;
        return response.data;
      } catch (error) {
        console.error('Error fetching project by ID:', error);
        throw error;
      }
    },

    async updateProject(id, projectDTO) {
      try {
        const response = await axios.put(`http://localhost:8080/api/v1/projects/${id}`, projectDTO);
        const updatedProject = response.data;
        const index = this.projects.findIndex(project => project.id === id);
        this.projects.splice(index, 1, updatedProject);
        return updatedProject;
      } catch (error) {
        console.error('Error updating project:', error);
        throw error;
      }
    },

    async deleteProject(id) {
      try {
        const response = await axios.delete('http://localhost:8080/api/v1/projects', 
        { 
          params: { id } 
        });
        console.log('Project deleted:', response.data);
        this.projects = this.projects.filter(project => project.id !== id);
      } catch (error) {
        console.error('Error deleting project:', error);
        throw error;
      }
    },
  },
});

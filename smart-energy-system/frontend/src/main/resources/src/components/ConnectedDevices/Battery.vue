<template>
  <b-card :title="`Battery (${id})`" class="border-0 bg-transparent">
    <b-card>
      <ul>
        <li v-for="(value, key) in item" :key="key">{{ key }}: {{ value }}</li>
      </ul>
      <p v-if="this.item.enabled">Battery is currently <strong>enabled</strong>!</p>
      <p v-if="!this.item.enabled">Battery is currently <strong>disabled</strong>!</p>
      <b-button
        v-on:click="toggleEnabled"
        variant="primary"
        :disabled="!this.buttonEnabled">Toggle Activation</b-button>
    </b-card>
  </b-card>
</template>

<script>
import Axios from 'axios';
import { mapGetters } from 'vuex';
import { GET_HOUSEHOLD } from '../../store/modules/current_household.methods';
import { API_HOUSEHOLDS } from '../../endpoints';

export default {
  name: 'Battery',
  props: ['id'],
  data() {
    return {
      item: {},
      buttonEnabled: true,
    };
  },
  mounted() {
    Axios.get(`${API_HOUSEHOLDS}/${this.household.id}/batteries/${this.id}`)
      .then((response) => {
        this.item = response.data;
      }).catch((error) => {
        console.log(error);
        this.$bus.$emit('backendOffline');
      });
  },
  computed: {
    ...mapGetters({
      household: GET_HOUSEHOLD,
    }),
  },
  methods: {
    toggleEnabled() {
      this.buttonEnabled = false;
      this.item.enabled = !this.item.enabled;
      // Axios update
      Axios.put(`${API_HOUSEHOLDS}/${this.household.id}/appliances/${this.item.key.id}`, this.item)
        .then((response) => {
          this.item = response.data;
          this.buttonEnabled = true;
        }).catch((error) => {
          console.log(error);
          this.$bus.$emit('backendOffline');
        });
    },
  },
};
</script>

<style scoped>

</style>

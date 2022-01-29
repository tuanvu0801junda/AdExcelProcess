<template>
  <div>
    <v-col align="center">
      <v-alert max-width="500" type="error">{{ errMessage }}</v-alert>
    </v-col>
  </div>
</template>

<script>
export default {
  name: "UploadAlert",
  data() {
    return {
      errMessage: "File Name must be ... ",
    };
  },
};
</script>

<style>
</style>
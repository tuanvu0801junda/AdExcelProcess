<template>
  <div>
    <v-col align="center">
      <h3>Uploaded file: {{ fileName }}</h3>
      <v-alert max-width="500" type="error">{{ errMessage }}</v-alert>
    </v-col>
  </div>
</template>

<script>
export default {
  name: "UploadAlert",
  props: {
    errMessage: String,
    fileName: String,
  },
};
</script>

<style>
</style>
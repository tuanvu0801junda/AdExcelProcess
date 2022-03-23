<template>
  <div>
    <v-col align="center">
      <h3>Uploaded file: <span style="color:red">{{ filename }}</span> had some error(s) !</h3>
      <v-btn class="ma-2" outlined color="red" @click="downloadFile()">
        Download Error.xlsx
        <v-icon right dark> mdi-cloud-download </v-icon>
      </v-btn>
    </v-col>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: "DownloadErrExcel",
  props: {
    filename: String,
  },
  methods: {
    async downloadFile() {
      var file_name = 'Error.xlsx';
      var url = `http://127.0.0.1:8080/file/download/${file_name}`;

      await axios.get(url, {responseType: 'arraybuffer'})
      .then(response => {
        // before: file-saver
        let blob = new Blob([response.data], { type: 'application/vnd.ms-excel' })
        let link = document.createElement('a')
        link.href = window.URL.createObjectURL(blob)
        link.download = file_name
        link.click()
      });
    }
  },
};
</script>

<style>
</style>

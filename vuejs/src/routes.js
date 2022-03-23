import Vue from "vue";
import VueRouter from "vue-router";
import App from "./App"
import AdGoogleAd from "./components/ggAd/AdGoogleAd";
import CampaignGoogleAd from "./components/ggAd/CampaignGoogleAd";
import KeywordGoogleAd from "./components/ggAd/KeywordGoogleAd";

Vue.use(VueRouter)

export const router = new VueRouter({
    routes: [
        { path: '/', component: App },
        { path: '/ggAd/campaign', component: CampaignGoogleAd },
        { path: '/ggAd/ad', component: AdGoogleAd },
        { path: '/ggAd/keyword', component: KeywordGoogleAd }
    ]
})

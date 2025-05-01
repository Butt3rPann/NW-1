import SaleItemList from "@/views/SaleItemList.vue";
import { createRouter, createWebHistory } from "vue-router";
const history = createWebHistory()
const routes = [
    {
        path: '/sale-items',
        name: 'SaleItems',
        component: SaleItemList
    }
]
const router = createRouter({history,routes})
export default router
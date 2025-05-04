import SaleItemList from "@/views/SaleItemList.vue";
import { createRouter, createWebHistory } from "vue-router";
import SaleItemsDetail from "@/views/SaleItemsDetail.vue";
const history = createWebHistory()
const routes = [
    {
        path: '/sale-items',
        name: 'SaleItems',
        component: SaleItemList
    },
    {
        path: '/sale-items/:saleItemId',
        name:'SaleItemsDetail',
        component: SaleItemsDetail
    },
]
const router = createRouter({history,routes})
export default router
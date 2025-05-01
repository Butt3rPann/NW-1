import { createRouter, createWebHistory } from "vue-router";
import demo1 from "../views/demo1.vue"
const history = createWebHistory()
const routes = [
    {
        path: '/sale-items',
        name: 'SaleItems',
        component: demo1
    }
]
const router = createRouter({history,routes})
export default router
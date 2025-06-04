import React, { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useParams } from "react-router-dom";
import { getMenuItemsByRestaurantId } from "../../State/Customers/Menu/menu.action";
import { fetchRestaurantsOrder } from "../../State/Admin/Order/restaurants.order.action"; // Import the action to fetch restaurant orders
import { Grid } from "@mui/material";
import OrdersTable from "../Orders/OrderTable";
import MenuItemTable from "../Food/MenuItemTable";

const RestaurantDashboard = () => {
  const { id } = useParams();
  const dispatch = useDispatch();

  useEffect(() => {
    dispatch(
      getMenuItemsByRestaurantId({
        restaurantId: id,
        jwt: localStorage.getItem("jwt"),
      })
    );

    // Fetch the latest 4 orders for the restaurant
    dispatch(
      fetchRestaurantsOrder({
        restaurantId: id,
        jwt: localStorage.getItem("jwt"),
        limit: 4, // Fetch only the latest 4 orders
      })
    );
  }, [dispatch, id]);

  return (
    <div className="px-2">
      <Grid container spacing={1}>
        <Grid lg={6} xs={12} item>
          <OrdersTable name={"Recent Order"} isDashboard={true} />
        </Grid>
        <Grid lg={6} xs={12} item>
          <MenuItemTable isDashboard={true} name={"Recently Added Menu"} />
        </Grid>
      </Grid>
    </div>
  );
};

export default RestaurantDashboard;

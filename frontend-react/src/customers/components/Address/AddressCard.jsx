import { Button, Card, IconButton } from "@mui/material";
import HomeIcon from "@mui/icons-material/Home";
import DeleteIcon from "@mui/icons-material/Delete";
import React from "react";

const AddressCard = ({ handleSelectAddress, handleDeleteAddress, item, showButton }) => {
  return (
    <Card className="flex space-x-5 w-64 p-5">
      <HomeIcon />

      <div className="space-y-3 text-gray-500">
        <h1 className="font-semibold text-lg text-white">Home</h1>
        <p>
          {item.streetAddress}, {item.postalCode}, {item.state}, {item.country}
        </p>

        {showButton && (
          <>
            <Button
              onClick={() => handleSelectAddress(item)}
              variant="outlined"
              className="w-full"
            >
              Select
            </Button>
            <IconButton
              onClick={() => handleDeleteAddress(item.id)}
              color="secondary"
            >
              <DeleteIcon />
            </IconButton>
          </>
        )}
      </div>
    </Card>
  );
};

export default AddressCard;

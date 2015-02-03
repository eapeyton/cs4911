module.exports = function(sequelize, DataTypes) {
  var Room = sequelize.define("Room", {
    id: DataTypes.INTEGER,
    name: DataTypes.STRING,
    createdAt: DataTypes.DATE,
    updatedAt: DataTypes.DATE
  }, {
    classMethods: {
      associate: function(models) {
      }
    }
  });

  return Room;
};
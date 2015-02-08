module.exports = function(sequelize, DataTypes) {
  var Room = sequelize.define("Room", {
    id: { 
      type: DataTypes.UUIDV4, 
      defaultValue: DataTypes.UUIDV4,
      unique: true,
      primaryKey: true
    },
    name: DataTypes.STRING,
    maxPlayers: DataTypes.INTEGER,
    createdAt: DataTypes.DATE,
    updatedAt: DataTypes.DATE
  }, {
    classMethods: {
      associate: function(models) {
        Room.hasMany(models.User, { foreignKey: 'rid' });
      }
    }
  });

  return Room;
};
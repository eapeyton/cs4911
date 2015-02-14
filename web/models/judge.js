module.exports = function(sequelize, DataTypes) {
  var Judge = sequelize.define("Judge", {
    id: { 
      type: DataTypes.UUID,
      defaultValue: DataTypes.UUIDV4,
      unique: true,
      primaryKey: true
    },
    userId: {
      type: DataTypes.UUID,
      references: 'Users',
      referencesKey: 'id',
      allowNull: false,
      unique: 'userRoomPlaceIndex'
    },
    roomId: {
      type: DataTypes.UUID,
      references: 'Rooms',
      referencesKey: 'id',
      allowNull: false,
      unique: 'userRoomPlaceIndex'
    },
    place: {
      type: DataTypes.INTEGER,
      allowNull: false,
      unique: 'userRoomPlaceIndex'
    },
    createdAt: DataTypes.DATE,
    updatedAt: DataTypes.DATE
  }, {
    classMethods: {
      associate: function(models) {
        Judge.belongsTo(models.User, { foreignKey: 'userId' });
        Judge.belongsTo(models.Room, { foreignKey: 'roomId' });
      }
    }
  });

  return Judge;
};
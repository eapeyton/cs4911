module.exports = function(sequelize, DataTypes) {
  var Host = sequelize.define("Host", {
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
      unique: 'userRoomIndex'
    },
    roomId: {
      type: DataTypes.UUID,
      references: 'Rooms',
      referencesKey: 'id',
      allowNull: false,
      unique: 'userRoomIndex'
    },
    createdAt: DataTypes.DATE,
    updatedAt: DataTypes.DATE
  }, {
    classMethods: {
      associate: function(models) {
        Host.belongsTo(models.User, { foreignKey: 'userId' });
        Host.belongsTo(models.Room, { foreignKey: 'roomId' });
      }
    }
  });

  return Host;
};
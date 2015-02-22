module.exports = function(sequelize, DataTypes) {
  var Game = sequelize.define("Game", {
    id: { 
      type: DataTypes.UUID,
      defaultValue: DataTypes.UUIDV4,
      unique: true,
      primaryKey: true
    },
    roomId: {
      type: DataTypes.UUID,
      references: 'Rooms',
      referencesKey: 'id',
      allowNull: false
    },
    finishTime: DataTypes.DATE,
    createdAt: DataTypes.DATE,
    updatedAt: DataTypes.DATE
  }, {
    classMethods: {
      associate: function(models) {
        Game.belongsTo(models.Room, { foreignKey: 'roomId' });
        Game.hasMany(models.Hand, { foreignKey: 'gameId' });
        Game.hasMany(models.Round, { foreignKey: 'gameId' });
        Game.hasMany(models.PlayerState, { foreignKey: 'gameId' });
      }
    }
  });

  return Game;
};
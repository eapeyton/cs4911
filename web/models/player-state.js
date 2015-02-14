module.exports = function(sequelize, DataTypes) {
  var PlayerState = sequelize.define("PlayerState", {
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
      unique: 'userGameIndex'
    },
    gameId: {
      type: DataTypes.UUID,
      references: 'Games',
      referencesKey: 'id',
      allowNull: false,
      unique: 'userGameIndex'
    },
    handId: {
      type: DataTypes.UUID,
      references: 'Hands',
      referencesKey: 'id',
      allowNull: false,
    },
    state: DataTypes.STRING,
    points: DataTypes.INTEGER,
    place: DataTypes.INTEGER,
    createdAt: DataTypes.DATE,
    updatedAt: DataTypes.DATE
  }, {
    classMethods: {
      associate: function(models) {
        PlayerState.belongsTo(models.User, { foreignKey: 'userId' });
        PlayerState.belongsTo(models.Game, { foreignKey: 'gameId' });
        PlayerState.belongsTo(models.Hand, { foreignKey: 'handId' });
      }
    }
  });

  return PlayerState;
};
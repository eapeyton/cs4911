module.exports = function(sequelize, DataTypes) {
  var Card = sequelize.define("Card", {
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
      allowNull: true
    },
    text: DataTypes.STRING,
    type: DataTypes.STRING,
    createdAt: DataTypes.DATE,
    updatedAt: DataTypes.DATE
  }, {
    classMethods: {
      associate: function(models) {
        Card.belongsTo(models.User, { foreignKey: 'userId' });
        Card.hasMany(models.Vote, { foreignKey: 'cardId' });
        Card.hasMany(models.Round, { foreignKey: 'blackCard' });
        Card.hasMany(models.Round, { foreignKey: 'winningCard' });
        Card.hasMany(models.PlayedCard, { foreignKey: 'cardId' });
        Card.hasMany(models.Hand, { foreignKey: 'cardId' });
      }
    }
  });

  return Card;
};
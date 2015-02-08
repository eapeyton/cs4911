module.exports = function(sequelize, DataTypes) {
  var Card = sequelize.define("Card", {
    id: { 
      type: DataTypes.UUIDV4, 
      defaultValue: DataTypes.UUIDV4,
      unique: true,
      primaryKey: true
    },
    uid: {
      type: DataTypes.UUIDV4,
      references: 'Users',
      referencesKey: 'id',
      allowNull: false
    },
    text: DataTypes.STRING,
    type: DataTypes.STRING,
    createdAt: DataTypes.DATE,
    updatedAt: DataTypes.DATE
  }, {
    classMethods: {
      associate: function(models) {
        Card.belongsTo(models.User, { foreignKey: 'uid' });
        Card.hasMany(models.Vote, { foreignKey: 'cid' });
      }
    }
  });

  return Card;
};
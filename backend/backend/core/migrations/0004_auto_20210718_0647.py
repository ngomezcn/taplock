# Generated by Django 3.2.4 on 2021-07-18 06:47

from django.db import migrations, models
import uuid


class Migration(migrations.Migration):

    dependencies = [
        ('core', '0003_auto_20210718_0647'),
    ]

    operations = [
        migrations.AlterField(
            model_name='itap',
            name='activation_key',
            field=models.CharField(default='GZMTN8SX', max_length=8),
        ),
        migrations.AlterField(
            model_name='itap',
            name='seed',
            field=models.CharField(default=uuid.UUID('18c69c8e-2c14-4a53-81fa-4cfff5f8f748'), max_length=36),
        ),
    ]
